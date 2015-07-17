package hale.bc.server.repository;

import hale.bc.server.repository.exception.DuplicatedEntryException;
import hale.bc.server.service.mail.MailSenderInfo;
import hale.bc.server.service.mail.MailSender;
import hale.bc.server.to.CodeSender;
import hale.bc.server.to.User;
import hale.bc.server.to.UserStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
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
	private Random r = new Random();
	
	private StringRedisTemplate stringTemplate;
	
	private RedisZSet<String> userNameIndex;
	private ValueOperations<String, String> userNames;
	private ValueOperations<String, String> userCodes;
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
		userNameIndex = new DefaultRedisZSet<String>(KeyUtils.users(), stringTemplate);
		userIdGenerator = new RedisAtomicLong(KeyUtils.userId(), stringTemplate.getConnectionFactory());
	}
	
	public String createUser(User user) throws DuplicatedEntryException {
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
		//send
		CodeSender sender = null;
		try {
			sender = getCodeSender();
		} catch (FileNotFoundException e) {
			return "{\"result\":0, \"errorMsg\":\"未找到配置文件！\"}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if ("true".equals(sender.getAutoSendCode())) {
			try {
				sendVerifyCodeMail(sender, userName, code);
			} catch (MessagingException e) {
				return "{\"result\":-11, \"errorMsg\":\"邮件发送失败！\"}";
			}
			return "{\"result\":10, \"msg\":\"验证码已发，请查看您的邮箱！\"}";
		} 
		
		return "{\"result\":1, \"msg\":\"用户申请注册成功！\"}";
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

	public String eidtPassword(User user, String newPwd) {
		String userNameKey = KeyUtils.userName(user.getName());
		if (stringTemplate.hasKey(userNameKey)) {
			String uid = userNames.get(userNameKey);
			User userInfo = users.get(KeyUtils.userId(uid));
			if (passwordEncoder.matches(user.getPassword(), userInfo.getPassword())) {
				userInfo.setPassword(passwordEncoder.encode(newPwd));
				users.set(KeyUtils.userId(uid), userInfo);
				return "{\"result\":1, \"msg\":\"密码修改成功！\"}";
			} else {
				return "{\"result\":-11, \"errorMsg\":\"原密码输入错误，请重新输入！\"}";
			}
		}
		return "{\"result\":0, \"errorMsg\":\"未知错误！\"}";
	}
	
	public String forgetPwd(String userName) {
		long timeCode = 0;
		String userNameKey = KeyUtils.userName(userName);
		if (stringTemplate.hasKey(userNameKey)) {
			String uid = userNames.get(userNameKey);
			User userInfo = users.get(KeyUtils.userId(uid));
			timeCode = new Date().getTime();
			userInfo.setTimeCode(timeCode);
			users.set(KeyUtils.userId(uid), userInfo);
		}
		
		CodeSender sender = null;
		try {
			sender = getCodeSender();
		} catch (FileNotFoundException e) {
			return "{\"result\":0, \"errorMsg\":\"未找到发送邮件参数配置文件！\"}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			sendResetPwdMail(sender, userName, timeCode);
		} catch (MessagingException e) {
			return "{\"result\":-11, \"errorMsg\":\"邮件发送失败！\"}";
		}
		return "{\"result\":1, \"msg\":\"密码重置的邮件已发送到您的邮箱，请在2天内重置密码！\"}";
	} 
	
	public String resetPwd(User user) {
		String userNameKey = KeyUtils.userName(user.getName());
		if (stringTemplate.hasKey(userNameKey)) {
			String uid = userNames.get(userNameKey);
			User userInfo = users.get(KeyUtils.userId(uid));
			if (userInfo.getTimeCode() == null) {
				return "{\"result\":-12, \"errorMsg\":\"请先提交找回密码申请！\"}";
			} else {
				if (StringUtils.isNotBlank(user.getTimeCode().toString()) && userInfo.getTimeCode().equals(user.getTimeCode())){
					if (new Date().getTime() - user.getTimeCode() <= 1000*60*60*24*2) {
						userInfo.setPassword(passwordEncoder.encode(user.getPassword()));
						users.set(KeyUtils.userId(uid), userInfo);
						return "{\"result\":1, \"msg\":\"密码重置成功！\"}";
					} else {
						return "{\"result\":-11, \"errorMsg\":\"密码重置链接过期，请重新申请重置密码！\"}";
					}
				} else {
					return "{\"result\":-12, \"errorMsg\":\"密码重置链接错误，请核对后重试！\"}";
				}
			}
		}
		return "{\"result\":0, \"errorMsg\":\"用户名不存在！\"}";
	} 
	
	public String initPwd(User user) {
		String userNameKey = KeyUtils.userName(user.getName());
		if (stringTemplate.hasKey(userNameKey)) {
			String uid = userNames.get(userNameKey);
			User userInfo = users.get(KeyUtils.userId(uid));
			userInfo.setPassword(passwordEncoder.encode(user.getPassword()));
			users.set(KeyUtils.userId(uid), userInfo);
			return "{\"result\":1, \"msg\":\"密码重置成功！\"}";
		}
		return "{\"result\":0, \"errorMsg\":\"未知错误！\"}";
	} 
	
	public CodeSender getCodeSender() throws FileNotFoundException, Exception{
		CodeSender codeSender = new CodeSender();
		SAXReader saxReader = new SAXReader();
	    Document document = saxReader.read(new File("config/mock_config.xml"));
	    for(Element e : (List<Element>)document.getRootElement().elements()){
	        codeSender.setAutoSendCode(e.element("auto_send_code").getText());
	        codeSender.setServerHost(e.element("server_host").getText());
	        codeSender.setServerPort(e.element("server_port").getText());
	        codeSender.setUsername(e.element("user_name").getText());
	        codeSender.setPassword(e.element("password").getText());
	    }
		return codeSender;
	}
	
	private void sendVerifyCodeMail(CodeSender sender, String userName, String code) throws MessagingException {
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(sender.getServerHost());
		mailInfo.setMailServerPort(sender.getServerPort());
		mailInfo.setValidate(true);
		mailInfo.setUserName(sender.getUsername());
		mailInfo.setPassword(sender.getPassword());
		mailInfo.setFromAddress(sender.getUsername());
		mailInfo.setToAddress(userName);
		mailInfo.setSubject("您获得了mock-api工具的注册码，非常感谢您的注册和关注");
		mailInfo.setContent("<!DOCTYPE html><html ><head lang=\"zh-CN\"></head>"
				+ "<body>"
				+ "亲爱的用户：<br>"
				+ "<p>非常感谢您注册我们的mock工具。您获得的注册码为：<span style=\"font-size: 24.0px;background-color: #f2dcdb;\">"+code+"</span>。</p>"
				+ "<p>您可以在 <a href='http://mock-api.com/#!/signup'>http://mock-api.com/#!/signup</a> 页面上点击<span style=\"font-size: 24.0px;background-color: yellow;\">中间黄色按钮</span>进入步骤II"
				+ "后输入上述注册码通过注册。</br>"
				+ "验证成功后，就可以使用您的用户名和密码登录该工具。</p>"
				+ "<p>因为本次公网上发布的是封测版本，有很多功能还不完善或者存在一些缺陷，大家使用过程中有任何"
				+ "疑问、建议、需求以及吐槽，甚至骂街都可以在以下github项目上提交issue:<br>"
				+ "<a href='https://github.com/mock-api-agilean/issue-tracking/issues'>https://github.com/mock-api-agilean/issue-tracking/issues</a><br>"
				+ "我们会尽最大努力答复并解决。<br>"
				+ "<p>Best regards<br>"
				+ "<a href='http://www.agilean.cn'>Agilean</a></p>"
				+ "</body></html>");
		MailSender sms = new MailSender();
		sms.sendHtmlMail(mailInfo);
	}
	
	private void sendResetPwdMail(CodeSender sender, String userName, long timeCode) throws MessagingException {
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(sender.getServerHost());
		mailInfo.setMailServerPort(sender.getServerPort());
		mailInfo.setValidate(true);
		mailInfo.setUserName(sender.getUsername());
		mailInfo.setPassword(sender.getPassword());
		mailInfo.setFromAddress(sender.getUsername());
		mailInfo.setToAddress(userName);
		mailInfo.setSubject("mock-api工具密码重置服务，非常感谢您的使用和关注");
		mailInfo.setContent("<!DOCTYPE html><html ><head lang=\"zh-CN\"></head>"
				+ "<body>"
				+ "亲爱的用户：<br>"
				+ "<p>非常感谢您使用我们的mock工具。</p>"
				+ "<p>密码重置链接为: <a href='http://mock-api.com/#!/reset-pwd?name=" + userName + "&r=" + timeCode + "'>http://mock-api.com/users/resetPwd?name=" + userName + "&r=" + timeCode + "</a>"
				+ "</br>"
				+ "密码重置成功后，就可以使用您的用户名和密码正常登录该工具。</p>"
				+ "<p>因为本次公网上发布的是封测版本，有很多功能还不完善或者存在一些缺陷，大家使用过程中有任何"
				+ "疑问、建议、需求以及吐槽，甚至骂街都可以在以下github项目上提交issue:<br>"
				+ "<a href='https://github.com/mock-api-agilean/issue-tracking/issues'>https://github.com/mock-api-agilean/issue-tracking/issues</a><br>"
				+ "我们会尽最大努力答复并解决。<br>"
				+ "<p>Best regards<br>"
				+ "<a href='http://www.agilean.cn'>Agilean</a></p>"
				+ "</body></html>");
		MailSender sms = new MailSender();
		sms.sendHtmlMail(mailInfo);
	}

}
