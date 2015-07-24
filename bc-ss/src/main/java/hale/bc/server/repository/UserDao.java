package hale.bc.server.repository;

import hale.bc.server.repository.exception.DuplicatedEntryException;
import hale.bc.server.repository.exception.OldPwdErrorException;
import hale.bc.server.repository.exception.ResetPwdLinkErrorException;
import hale.bc.server.to.User;
import hale.bc.server.to.UserStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.data.redis.support.collections.DefaultRedisZSet;
import org.springframework.data.redis.support.collections.RedisZSet;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
	private final long REPIRE_TIME = 1000*60*60*24*2;

	private Random r = new Random();
	
	private StringRedisTemplate stringTemplate;
	
	private RedisZSet<String> userNameIndex;
	private ValueOperations<String, String> userNames;
	private ValueOperations<String, String> userCodes;
	private ValueOperations<String, String> pwdCodes;
	private ValueOperations<String, User> users;
	private RedisAtomicLong userIdGenerator;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserDao(RedisTemplate<String, User> userTemplate, StringRedisTemplate template) {
		stringTemplate = template;
		users = userTemplate.opsForValue();
		userNames = stringTemplate.opsForValue();
		userCodes = stringTemplate.opsForValue();
		pwdCodes = stringTemplate.opsForValue();
		userNameIndex = new DefaultRedisZSet<String>(KeyUtils.users(), stringTemplate);
		userIdGenerator = new RedisAtomicLong(KeyUtils.userId(), stringTemplate.getConnectionFactory());
	}
	
	public User createUser(User user) throws DuplicatedEntryException {
		String userName = user.getName();
		String userNameKey = KeyUtils.userName(userName);
		if (userNameIndex.contains(userName) || stringTemplate.hasKey(userNameKey)) {
			throw new DuplicatedEntryException(userNameKey);
		}
			
		long uid = userIdGenerator.incrementAndGet();
		user.setId(uid);
		String userId = String.valueOf(uid);
		
		String code = null;
		do {
			 code = String.valueOf(r.nextInt(900000) + 100000);
		} while (stringTemplate.hasKey(KeyUtils.userCode(code)));
		userCodes.set(KeyUtils.userCode(code), userId);
		user.setCode(code);
		users.set(KeyUtils.userId(userId), user);
		userNames.set(userNameKey, userId);
		userNameIndex.add(userName, 0);
		
		return user;
	}

	public User activeUser(String code) {
		String userCodeKey = KeyUtils.userCode(code);
		if (stringTemplate.hasKey(userCodeKey)) {
			String uid = userCodes.get(userCodeKey);
			String userIdKey = (KeyUtils.userId(uid));
			User u = users.get(userIdKey);
			if (u.getStatus() == UserStatus.New) {
				u.setStatus(UserStatus.Active);
				users.set(userIdKey, u);
			}
			stringTemplate.delete(userCodeKey);
			return u;
		}
		return null;
	}
	
	public User updateUserStatus(Long uid, UserStatus status) {
		String userIdKey = KeyUtils.userId(String.valueOf(uid));
		if (stringTemplate.hasKey(userIdKey)) {
			User u = users.get(userIdKey);
			u.setStatus(status);
			users.set(userIdKey, u);
			return u;
		}
		return null;
	}
	
	public User getUserByName(String name) {
		String userNameKey = KeyUtils.userName(name);
		if (stringTemplate.hasKey(userNameKey)) {
			String uid = userNames.get(userNameKey);
			return users.get(KeyUtils.userId(uid));
		}
		return null;
	}

	public User getUserById(Long uid) {
		String userIdKey = KeyUtils.userId(String.valueOf(uid));
		if (stringTemplate.hasKey(userIdKey)) {
			return users.get(userIdKey);
		}
		return null;
	}

	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		for (String username : userNameIndex) {
			users.add(getUserByName(username));
		}
		return users;
	}
	
	public User eidtPassword(User user) throws OldPwdErrorException {
		String userNameKey = KeyUtils.userName(user.getName());
		if (stringTemplate.hasKey(userNameKey)) {
			String uid = userNames.get(userNameKey);
			User userInfo = users.get(KeyUtils.userId(uid));
			if (passwordEncoder.matches(user.getPassword(), userInfo.getPassword())) {
				userInfo.setPassword(passwordEncoder.encode(user.getNewPwd()));
				users.set(KeyUtils.userId(uid), userInfo);
				return userInfo;
			} else {
				throw new OldPwdErrorException();
			}
		}
		return null;
	}
	
	public User getUserByPwdCode(String code) {
		String pwdCodeKey = KeyUtils.resetPwdCode(code);
		if (stringTemplate.hasKey(pwdCodeKey)) {
			String uid = pwdCodes.get(pwdCodeKey);
			String userIdKey = (KeyUtils.userId(uid));
			User u = users.get(userIdKey);
			return u;
		}
		return null;
	}
	
	public User forgetPwd(String userName) {
		String pwdCode="";
		String userNameKey = KeyUtils.userName(userName);
		if (stringTemplate.hasKey(userNameKey)) {
			String uid = userNames.get(userNameKey);
			User userInfo = users.get(KeyUtils.userId(uid));
			pwdCode = UUID.randomUUID().toString();
			pwdCodes.set(KeyUtils.resetPwdCode(pwdCode), uid);
			
			userInfo.setPwdCode(pwdCode);
			userInfo.setExpireTime(new Date().getTime());
			users.set(KeyUtils.userId(uid), userInfo);

			return userInfo;
		}
		return null;
	} 
	
	public User resetPwd(User user) throws ResetPwdLinkErrorException {
		String userNameKey = KeyUtils.userName(user.getName());
		if (stringTemplate.hasKey(userNameKey)) {
			String uid = userNames.get(userNameKey);
			User userInfo = users.get(KeyUtils.userId(uid));
			if (userInfo.getPwdCode() == null) {
				throw new ResetPwdLinkErrorException();
			} else {
				if (userInfo.getPwdCode().equals(user.getPwdCode())){
					if (new Date().getTime() - userInfo.getExpireTime() <= REPIRE_TIME) {
						userInfo.setPassword(passwordEncoder.encode(user.getPassword()));
						users.set(KeyUtils.userId(uid), userInfo);
						stringTemplate.delete(KeyUtils.resetPwdCode(userInfo.getPwdCode()));
						return userInfo;
					} else {
						throw new ResetPwdLinkErrorException();
					}
				} else {
					throw new ResetPwdLinkErrorException();
				}
			}
		}
		return null;
	} 
	
}
