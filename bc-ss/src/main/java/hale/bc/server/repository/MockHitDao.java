package hale.bc.server.repository;

import hale.bc.server.to.MockHit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Repository
public class MockHitDao {

	private ZSetOperations<String, MockHit> activityCodeIndex;
	
	@Autowired
	public MockHitDao(RedisTemplate<String, MockHit> mockHitTemplate) {
		activityCodeIndex = mockHitTemplate.opsForZSet();
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
}
