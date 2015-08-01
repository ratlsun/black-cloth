package hale.bc.server.service;

import hale.bc.server.repository.KeyUtils;
import hale.bc.server.repository.MockActivityDao;
import hale.bc.server.service.event.RuleChangedEvent;
import hale.bc.server.to.MockActivity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RuleChangedListenerByActivity implements ApplicationListener<RuleChangedEvent> {

	@Autowired
	private MockActivityService mockActivityService;
	
	@Autowired
	private MockActivityDao mockActivityDao;
	
	@Autowired
	private StringRedisTemplate template;
	
	@Override
	public void onApplicationEvent(RuleChangedEvent event) {
		Long mid = event.getRule().getMockerId();
		for (String macode: template.opsForSet().members(KeyUtils.mockActivityMocker(mid))) {
			MockActivity ma = mockActivityDao.getMockActivityByCode(macode);
			if (ma != null && ma.getAutoLoading()){
				mockActivityService.clearRules(ma);
				mockActivityService.loadRules(ma);
			}
		}
	}

	public void registerMockActivity(MockActivity activity) {
		for (Long mid: activity.getMockerIds()) {
			template.opsForSet().add(KeyUtils.mockActivityMocker(mid), activity.getCode());
		}
	}
	
	public void unregisterMockActivity(MockActivity activity) {
		for (Long mid: activity.getMockerIds()) {
			template.opsForSet().remove(KeyUtils.mockActivityMocker(mid), activity.getCode());
		}
	}
}
