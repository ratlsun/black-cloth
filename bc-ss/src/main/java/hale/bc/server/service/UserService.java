package hale.bc.server.service;

import hale.bc.server.repository.UserDao;
import hale.bc.server.repository.exception.DuplicatedEntryException;
import hale.bc.server.to.Mail;
import hale.bc.server.to.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private MailSenderService mailSenderService;

	@Autowired
	private Mail mail;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = userDao.getUserByName(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		List<SimpleGrantedAuthority> roles = new ArrayList<>();
		for (String r : user.getRoles()) {
			roles.add(new SimpleGrantedAuthority("ROLE_" + r));
		}
		return new org.springframework.security.core.userdetails.User(
				user.getName(), user.getPassword(), user.isActive(), true,
				true, true, roles);
	}

	public User createUser(User user) throws DuplicatedEntryException,
			MessagingException {
		User u = userDao.createUser(user);
		if ("true".equals(mail.getAutoSendCode())) {
			Mail m = new Mail();
			m.setTo(user.getName());
			m.setSubject("欢迎注册mock-api工具用户，非常感谢您的使用和关注");
			m.setTemplateName("mail-code.vm");
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("code", user.getCode());
			try {
				mailSenderService.sendWithTemplate(model, m);
			} catch (Exception e) {
				throw new MessagingException();
			}
		}
		return u;
	}

	public User forgetPwd(String name) throws MessagingException {
		User u = userDao.forgetPwd(name);
		if (u != null) {
			Mail m = new Mail();
			m.setTo(u.getName());
			m.setSubject("欢迎使用mock-api工具密码重置服务，非常感谢您的使用和关注");
			m.setTemplateName("mail-reset-pwd.vm");
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("url", "http://mock-api.com/#!/reset-pwd?r=" + u.getPwdCode() );
			try {
				mailSenderService.sendWithTemplate(model, m);
			} catch (Exception e) {
				throw new MessagingException();
			}
			
			return u;
		} else {
			return null;
		}
	}

	public User resendCode(User user) throws MessagingException {
		Mail m = new Mail();
		m.setTo(user.getName());
		m.setSubject("欢迎注册mock-api工具用户，非常感谢您的使用和关注");
		m.setTemplateName("mail-code.vm");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("code", user.getCode());
		try {
			mailSenderService.sendWithTemplate(model, m);
		} catch (Exception e) {
			throw new MessagingException();
		}
		return user;
	}

}
