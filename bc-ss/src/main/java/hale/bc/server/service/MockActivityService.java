package hale.bc.server.service;

import hale.bc.server.repository.RuleDao;
import hale.bc.server.to.MockActivity;
import hale.bc.server.to.Rule;
import hale.bc.server.to.RuleRequest;
import hale.bc.server.to.RuleRequestBody;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MockActivityService {

	@Autowired
	private RuleDao ruleDao;
	
	@Autowired
	private RedisTemplate<String, Rule> template;

	public void loadRules(MockActivity activity) {

		ObjectMapper mapper = new ObjectMapper();

		for (Long mid : activity.getMockerIds()) {
			List<Rule> rules = ruleDao.getRulesByMocker(mid);
			for (Rule rule : rules) {
				StringBuffer key = null;
				try {
					key = new StringBuffer(activity.getCode());
					key.append(':');
					key.append(mapper.writeValueAsString(rule.getRequest()
							.getHeader()));
					key.append(':');
					switch (rule.getRequest().getBody().getType()) {
					case Text:
						key.append(rule.getRequest().getBody().getContent());
						break;
					case Json:
						key.append(mapper.writeValueAsString(mapper
								.readTree(rule.getRequest().getBody()
										.getContent())));
						break;
					default:
					}
				} catch (JsonProcessingException e) {
					// eaten, this rule does not be load.
					e.printStackTrace();
				} catch (IOException e) {
					// eaten, this rule does not be load.
					e.printStackTrace();
				}
				if (key != null) {
					template.opsForValue().set(key.toString(), rule);
				}
			}
		}
	}
	
	public void clearRules(MockActivity activity) {
		String code = activity.getCode();
		template.delete(template.keys(code + ":*"));
	}

	public Rule match(String code, RuleRequest request) {
		Rule result = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			StringBuffer key = new StringBuffer(code);
			key.append(':');
			key.append(mapper.writeValueAsString(request.getHeader()));
			key.append(':');
			String hkey = key.toString();
			RuleRequestBody body = request.getBody();
			if (body != null) {
				result = matchText(hkey, body.getContent());
				if (result != null) {
					return result;
				}
				result = matchJson(hkey, body.getContent(), mapper);
				if (result != null) {
					return result;
				}
			}
			result = template.opsForValue().get(hkey);
		} catch (JsonProcessingException e) {
			// eaten
			e.printStackTrace();
		} 
		return result;
	}
	
	private Rule matchJson(String prefix, String content, ObjectMapper mapper) {
		try {
			String suffix = mapper.writeValueAsString(mapper.readTree(content));
			return template.opsForValue().get(prefix + suffix);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Rule matchText(String prefix, String content){
		return template.opsForValue().get(prefix + content);
	}
	
}
