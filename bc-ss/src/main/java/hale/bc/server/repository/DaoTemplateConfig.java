package hale.bc.server.repository;

import hale.bc.server.to.Channel;
import hale.bc.server.to.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class DaoTemplateConfig {

	@Autowired
	private RedisConnectionFactory jedis;
	
	@Bean
	public RedisTemplate<String, User> userTemplate() {
		return template(User.class);
	}
	
	@Bean
	public RedisTemplate<String, Channel> channelTemplate() {
		return template(Channel.class);
	}

	private <T> RedisTemplate<String, T> template(Class<T> t) {
		RedisTemplate<String, T> template = new RedisTemplate<>();
		template.setConnectionFactory(jedis);
		template.setKeySerializer(template.getStringSerializer());
		template.setValueSerializer(new Jackson2JsonRedisSerializer<T>(t));
		return template;
	}
}
