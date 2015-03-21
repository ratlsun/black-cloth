package hale.bc.server.repository;

import hale.bc.server.repository.exception.DuplicatedEntryException;
import hale.bc.server.to.User;
import hale.bc.server.to.UserStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.data.redis.support.collections.DefaultRedisZSet;
import org.springframework.data.redis.support.collections.RedisZSet;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

	private Random r = new Random();
	
	private StringRedisTemplate stringTemplate;
	
	private RedisZSet<String> userNameIndex;
	private ValueOperations<String, String> userNames;
	private ValueOperations<String, String> userCodes;
	private ValueOperations<String, User> users;
	private RedisAtomicLong userIdGenerator;
	
	@Autowired
	public UserDao(RedisTemplate<String, User> userTemplate, StringRedisTemplate template) {
		stringTemplate = template;
		users = userTemplate.opsForValue();
		userNames = stringTemplate.opsForValue();
		userCodes = stringTemplate.opsForValue();
		userNameIndex = new DefaultRedisZSet<String>(KeyUtils.users(), stringTemplate);
		userIdGenerator = new RedisAtomicLong(KeyUtils.uid(), stringTemplate.getConnectionFactory());
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

}
