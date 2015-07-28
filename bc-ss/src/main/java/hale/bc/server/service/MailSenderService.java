package hale.bc.server.service;

import hale.bc.server.to.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Service
public class MailSenderService {
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private VelocityEngine velocityEngine;

	public void sendPasswordResettingMail(User user) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setTo(user.getName());
			helper.setFrom("chenhao@agilean.cn");
			helper.setBcc("chenhao@agilean.cn");
			helper.setSubject("mock-api工具密码重置邮件，请在48小时内完成密码重置。");
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("url", "http://mock-api.com/#!/password-admin?r=" + user.getPwdCode());
			model.put("dt", format.format(new Date(user.getPwdCodeGeneratedTime())));
			helper.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, 
					"mail-template/mail-reset-pwd.vm", "UTF-8", model), true);
		} catch (MessagingException e) {
			// Eaten
			e.printStackTrace();
		}
		
		mailSender.send(message);
	}

	public void sendNewUserInviteMail(User newUser) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setTo(newUser.getName());
			helper.setFrom("chenhao@agilean.cn");
			helper.setBcc("chenhao@agilean.cn");
			helper.setSubject("您获得了mock-api工具的注册码，非常感谢您的注册和关注。");
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("code", newUser.getCode());
			helper.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, 
					"mail-template/mail-invite.vm", "UTF-8", model), true);
		} catch (MessagingException e) {
			// Eaten
			e.printStackTrace();
		}
		
		mailSender.send(message);
		
	}
}
