package hale.bc.server.repository;

import hale.bc.server.to.MockActivity;
import hale.bc.server.to.MockActivityStatus;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.Base64Utils;

@Repository
public class MockActivityDao {

	private SecureRandom random  = new SecureRandom();
	
	private StringRedisTemplate stringTemplate;
	
	private ZSetOperations<String, String> ownerIndex;
	private ValueOperations<String, MockActivity> activities;
	
	@Autowired
	public MockActivityDao(RedisTemplate<String, MockActivity> activityTemplate, StringRedisTemplate template) {
		stringTemplate = template;
		activities = activityTemplate.opsForValue();
		ownerIndex = stringTemplate.opsForZSet();
	}
	
	public MockActivity createMockActivity(MockActivity activity)  {
		//String code = String.valueOf(UUID.randomUUID());
		byte bytes[] = new byte[12];
	    random.nextBytes(bytes);
	    String code = Base64Utils.encodeToString(bytes).replaceAll("/", "-");
		activity.setCode(code);
		Date d = new Date();
		activity.setCreated(d);
		activity.setUpdated(d);
		activity.setStatus(MockActivityStatus.Running);
		
		activities.set(KeyUtils.mockActivityCode(code), activity);
		ownerIndex.add(KeyUtils.mockActivityOwner(activity.getOwner()), code, d.getTime());
		
		return activity;
	}
	
	public MockActivity getMockActivityByCode(String code) {
		return activities.get(KeyUtils.mockActivityCode(code));
	}
	
	public MockActivity getActiveMockActivityByOwner(String owner) {
		Set<String> codes =  ownerIndex.reverseRange(KeyUtils.mockActivityOwner(owner), 0, 1);
		if (codes != null && !codes.isEmpty()) {
			MockActivity ma = activities.get(KeyUtils.mockActivityCode((new ArrayList<String>(codes)).get(0)));
			if (ma.getStatus() != MockActivityStatus.Stopped) {
				return ma;
			}
		}
		return null;
	}

	public MockActivity updateMockActivity(MockActivity activity) {
		String code = activity.getCode();
		MockActivity oldMa = getMockActivityByCode(code);
		if (oldMa == null) {
			return null;
		}
		activity.setUpdated(new Date());
		activities.set(KeyUtils.mockActivityCode(code), activity);
		return activity;
	}
	
}
