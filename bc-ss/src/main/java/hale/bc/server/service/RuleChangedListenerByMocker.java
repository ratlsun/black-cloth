package hale.bc.server.service;

import hale.bc.server.repository.MockerDao;
import hale.bc.server.repository.RuleDao;
import hale.bc.server.repository.exception.DuplicatedEntryException;
import hale.bc.server.service.event.RuleChangedEvent;
import hale.bc.server.to.Mocker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class RuleChangedListenerByMocker implements ApplicationListener<RuleChangedEvent> {

	@Autowired
	private MockerDao mockerDao;
	
	@Autowired
	private RuleDao ruleDao;
	
	@Override
	public void onApplicationEvent(RuleChangedEvent event) {
		Long mid = event.getRule().getMockerId();
		Mocker mocker = mockerDao.getMockerById(mid);
		if (mocker != null) {
			mocker.setRuleCount(ruleDao.getRuleCountByMocker(mid));
			try {
				mockerDao.updateMocker(mocker);
			} catch (DuplicatedEntryException e) {
				e.printStackTrace();
			}
		}
	}

}
