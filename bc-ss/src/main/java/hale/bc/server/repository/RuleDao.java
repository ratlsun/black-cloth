package hale.bc.server.repository;

import hale.bc.server.service.event.RuleChangedEvent;
import hale.bc.server.to.Rule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class RuleDao implements ApplicationEventPublisherAware {

	private ApplicationEventPublisher eventPublisher;

	private StringRedisTemplate stringTemplate;
	
	private ZSetOperations<String, String> mockerIndex;
	private ValueOperations<String, Rule> rules;
	private RedisAtomicLong ruleIdGenerator;
	
	@Autowired
	public RuleDao(RedisTemplate<String, Rule> ruleTemplate, StringRedisTemplate template) {
		stringTemplate = template;
		rules = ruleTemplate.opsForValue();
		mockerIndex = stringTemplate.opsForZSet();
		ruleIdGenerator = new RedisAtomicLong(KeyUtils.ruleId(), stringTemplate.getConnectionFactory());
	}
	
	public Rule createRule(Rule rule)  {
		long rid = ruleIdGenerator.incrementAndGet();
		rule.setId(rid);
		String ruleId = String.valueOf(rid);
		
		Date d = new Date();
		rule.setCreated(d);
		rule.setUpdated(d);
		rules.set(KeyUtils.ruleId(ruleId), rule);
		mockerIndex.add(KeyUtils.ruleMocker(rule.getMockerId()), ruleId, rule.getUpdated().getTime());
		
		eventPublisher.publishEvent(new RuleChangedEvent(rule));
		
		return rule;
	}
	
	public Long getRuleCountByMocker(Long mockerId) {
		return mockerIndex.size(KeyUtils.ruleMocker(mockerId));
	}
	
	public List<Rule> getRulesByMocker(Long mockerId) {
		List<Rule> result = new ArrayList<>();
		for (String ruleId : mockerIndex.reverseRange(KeyUtils.ruleMocker(mockerId), 0, -1)){
			Rule r = rules.get(KeyUtils.ruleId(ruleId));
			if (r != null) {
				result.add(r);
			}
		}
		return result;
	}
	
	public Rule getRuleById(Long ruleId) {
		return rules.get(KeyUtils.ruleId(String.valueOf(ruleId)));
	}
	
	public Rule updateRule(Rule rule) {
		Long ruleId = rule.getId();
		Rule oldRule = getRuleById(ruleId);
		if (oldRule == null || oldRule.getMockerId() != rule.getMockerId()) {
			return null;
		}
		rule.setUpdated(new Date());
		rules.set(KeyUtils.ruleId(String.valueOf(ruleId)), rule);
		mockerIndex.add(KeyUtils.ruleMocker(rule.getMockerId()), String.valueOf(ruleId), rule.getUpdated().getTime());
		
		eventPublisher.publishEvent(new RuleChangedEvent(rule));
		
		return rule;
	}
	
	public Rule deleteRule(Long ruleId) {
		Rule r = getRuleById(ruleId);
		if (r == null) {
			return null;
		}
		r.setUpdated(new Date());
		mockerIndex.remove(KeyUtils.ruleMocker(r.getMockerId()), String.valueOf(ruleId));
		stringTemplate.delete(KeyUtils.ruleId(String.valueOf(ruleId)));
		rules.set(KeyUtils.ruleId(String.valueOf(-ruleId)), r);
		
		eventPublisher.publishEvent(new RuleChangedEvent(r));
		
		return r;
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
}
