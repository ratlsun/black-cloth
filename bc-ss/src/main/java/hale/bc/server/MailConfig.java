package hale.bc.server;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource(value = "classpath:mail.properties")
public class MailConfig {

	@Value("${mail.aliyun.host}")
	private String host;

	@Value("${mail.aliyun.port}")
	private int port;

	@Value("${mail.aliyun.username}")
	private String username;
	
	@Value("${mail.aliyun.password}")
	private String password;
	
	
	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setJavaMailProperties(getMailProperties());
		sender.setHost(host);
		sender.setPort(port);
		sender.setUsername(username);
		sender.setPassword(password);
		sender.setDefaultEncoding("UTF-8");
		return sender;
	}
	
	private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        return properties;
    }
	
}
