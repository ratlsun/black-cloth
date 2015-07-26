package hale.bc.server.service;

import hale.bc.server.to.Mail;

import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Service
public class MailSenderService {
	private JavaMailSender mailSender;
	private VelocityEngine velocityEngine;
	private SimpleMailMessage simpleMailMessage;

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public SimpleMailMessage getSimpleMailMessage() {
		return simpleMailMessage;
	}

	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessage = simpleMailMessage;
	}

	public void sendWithTemplate(Map<String, Object> model, Mail mailInfo) {
		simpleMailMessage.setTo(mailInfo.getTo());
		simpleMailMessage.setFrom(simpleMailMessage.getFrom());
		simpleMailMessage.setSubject(mailInfo.getSubject());
		String result = null;
		result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				mailInfo.getTemplateName(), "UTF-8", model);
		simpleMailMessage.setText(result);
		mailSender.send(simpleMailMessage);
	}
}
