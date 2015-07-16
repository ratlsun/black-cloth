package hale.bc.server.repository;

import hale.bc.server.to.MockHit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Repository
public class MockHitDao {

	private ZSetOperations<String, MockHit> activityCodeIndex;
	private StringRedisTemplate stringTemplate;
	
	@Autowired
	public MockHitDao(RedisTemplate<String, MockHit> mockHitTemplate, StringRedisTemplate stringTemplate) {
		activityCodeIndex = mockHitTemplate.opsForZSet();
		this.stringTemplate = stringTemplate;
	}
	
	public MockHit createMockHit(MockHit hit) {
		hit.setCreated(new Date());
		activityCodeIndex.add(KeyUtils.mockHitCode(hit.getMockActivityCode()), hit, hit.getCreated().getTime());
		return hit;
	}
	
	public List<MockHit> getMockHitsByActivityCode(String code, int count) {
		Set<MockHit> mhs = activityCodeIndex.reverseRange(KeyUtils.mockHitCode(code), 0, count);
		return new ArrayList<MockHit>(mhs);
	}

	public void clearMockHitByCode(String code) {
		String mockHitCodeKey = KeyUtils.mockHitCode(code);
		if (stringTemplate.hasKey(mockHitCodeKey)) {
			stringTemplate.delete(mockHitCodeKey);
		}
	}
}
