package hale.bc.server.service;

import hale.bc.server.repository.RuleDao;
import hale.bc.server.to.MockActivity;
import hale.bc.server.to.Rule;
import hale.bc.server.to.RuleRequest;
import hale.bc.server.to.RuleRequestBody;
import hale.bc.server.to.RuleRequestHeader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MockActivityService {

	private final static String BASE_URL_KEY_SIGN = "B:";
	
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
				StringBuffer baseKey = null;
				try {
					key = new StringBuffer(activity.getCode());
					key.append(':');
					RuleRequestHeader header = rule.getRequest().getHeader();
					
					baseKey = new StringBuffer(key);
					baseKey.append(BASE_URL_KEY_SIGN);
					baseKey.append(mapper.writeValueAsString(RuleRequestHeader.buildHeader(
							header.getBaseUrl(), header.getContentType(), header.getMethod())));
					baseKey.append(':');
					
					key.append(mapper.writeValueAsString(header));
					key.append(':');
					switch (rule.getRequest().getBody().getType()) {
					case Text:
						key.append(rule.getRequest().getBody().getContent());
						baseKey.append(rule.getRequest().getBody().getContent());
						break;
					case Json:
						try {
							key.append(mapper.writeValueAsString(mapper
									.readTree(rule.getRequest().getBody()
											.getContent())));
							baseKey.append(mapper.writeValueAsString(mapper
									.readTree(rule.getRequest().getBody()
											.getContent())));
						} catch (JsonProcessingException e) {
							// eaten, this rule loaded as empty body.
							e.printStackTrace();
						} catch (IOException e) {
							// eaten, this rule loaded as empty body.
							e.printStackTrace();
						}
						break;
					default:
					}
				} catch (JsonProcessingException e) {
					// eaten, this rule does not be load.
					e.printStackTrace();
					continue;
				} 
				if (key != null && baseKey != null) {
					template.opsForValue().set(key.toString(), rule);
					template.opsForList().leftPush(baseKey.toString(), rule);
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
		String key = getMatchKey(code, request.getHeader(), request.getBody(), true, false);
		result = matchFullUrl(key);
		if (result == null) {
			key = getMatchKey(code, request.getHeader(), request.getBody(), false, false);
			result = matchFullUrl(key);
		} 
		if (result == null && request.getHeader().hasUrlParameter()) {
			RuleRequestHeader header = request.getHeader();
			RuleRequestHeader baseHeader = RuleRequestHeader.buildHeader(
					header.getBaseUrl(), header.getContentType(), header.getMethod());
			key = getMatchKey(code, baseHeader, request.getBody(), true, true);
			result = matchBaseUrl(request, key);
			
			if (result == null) {
				key = getMatchKey(code, baseHeader, request.getBody(), false, true);
				result = matchBaseUrl(request, key);
			} 
		} 
		return result;
	}
	
	private String getMatchKey(String code, RuleRequestHeader header, RuleRequestBody body, boolean isText, boolean isBaseUrl) {
		StringBuffer key = new StringBuffer(code);
		try {
			ObjectMapper mapper = new ObjectMapper();
			key.append(':');
			if (isBaseUrl) {
				key.append(BASE_URL_KEY_SIGN);
			}
			key.append(mapper.writeValueAsString(header));
			key.append(':');
			if (body == null || body.getContent() == null || body.getContent().isEmpty()) {
				return key.toString();
			}
			if (isText) {
				key.append(body.getContent());
			} else {
				key.append(mapper.readTree(body.getContent()));
			}
		} catch (JsonProcessingException e) {
			// eaten
			e.printStackTrace();
		} catch (IOException e) {
			// eaten
			e.printStackTrace();
		}
		return key.toString();
	}
	
	private Rule matchBaseUrl(RuleRequest request, String key) {
		Rule result = null;
		List<Rule> rules = template.opsForList().range(key, 0, -1);
		if (rules == null || rules.isEmpty()){
			return null;
		}
		Map<String, String> reqParas = request.getHeader().getUrlParameters();
		int paraCount = reqParas.size();
		int maxCount = -1;
		for (Rule r : rules) {
			int matchCount = 0;
			boolean matched = true;
			RuleRequestHeader header = r.getRequest().getHeader();
			if (header.hasUrlParameter()) {
				Map<String, String> paras = header.getUrlParameters();
				for (String p: paras.keySet()) {
					if (reqParas.containsKey(p) && reqParas.get(p).equals(paras.get(p))){
						matchCount++;
					} else {
						matched = false;
						break;
					}
				}
			}
			if (!matched) {
				continue;
			}
			if (matchCount == paraCount) {
				result = r;
				break;
			}
			if (matchCount > maxCount) {
				maxCount = matchCount;
				result = r;
			}
		}
		return result;
	}
	
	private Rule matchFullUrl(String key) {
		return template.opsForValue().get(key);
	}
	
}
