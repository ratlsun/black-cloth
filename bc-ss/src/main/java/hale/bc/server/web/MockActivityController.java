package hale.bc.server.web;

import hale.bc.server.repository.MockActivityDao;
import hale.bc.server.service.MockActivityService;
import hale.bc.server.service.UserOperationService;
import hale.bc.server.to.FailedResult;
import hale.bc.server.to.MockActivity;
import hale.bc.server.to.MockActivityStatus;
import hale.bc.server.to.UserOperation;
import hale.bc.server.to.UserOperationType;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/mock-activities")
public class MockActivityController {
	
	@Autowired
	private MockActivityDao mockActivityDao;
	
	@Autowired
	private UserOperationService userOperationService;
	
	@Autowired
	private MockActivityService mockActivityService;
	
	@RequestMapping(value = "/active", method=RequestMethod.GET)
    public MockActivity getActiveMockByOwner(Principal principal) {
		return mockActivityDao.getActiveMockActivityByOwner(principal.getName());
    }
	
	@RequestMapping(value = "/has", method=RequestMethod.GET)
    public FailedResult hasMockByOwner(Principal principal) {
		Set<String> codes = mockActivityDao.getMockCodesByOwner(principal.getName(), 1);
		if (codes == null || codes.isEmpty()) {
			return null;
		}
		return new FailedResult(0, codes.iterator().next());  //@TODO change to normal result class.
    }
	
	@RequestMapping(method=RequestMethod.POST)
    public MockActivity start(@RequestBody MockActivity activity, Principal principal) {
		activity.setOwner(principal.getName());
		MockActivity ma = mockActivityDao.createMockActivity(activity);
		if (ma != null && ma.getMockerIds() != null && !ma.getMockerIds().isEmpty()) {
			mockActivityService.loadRules(ma);
			userOperationService.log(UserOperation.activityOperation(principal.getName(), ma.getCode(), 
					UserOperationType.StartMock));
		}
		return ma;
    }
	
	@RequestMapping(value = "/{code}/pause", method=RequestMethod.PUT)
    public MockActivity pause(@PathVariable String code, Principal principal) {
		MockActivity ma = updateStatus(code, principal.getName(), MockActivityStatus.Paused);
		if (ma != null) {
			mockActivityService.clearRules(ma);
			userOperationService.log(UserOperation.activityOperation(principal.getName(), ma.getCode(), 
					UserOperationType.PauseMock));
		}
		return ma;
    }

	@RequestMapping(value = "/{code}/resume", method=RequestMethod.PUT)
    public MockActivity resume(@PathVariable String code, @RequestBody MockActivity activity, Principal principal) {
		MockActivity ma = updateStatus(code, principal.getName(), MockActivityStatus.Running, activity.getMockerIds());
		if (ma != null && ma.getMockerIds() != null && !ma.getMockerIds().isEmpty()) {
			mockActivityService.loadRules(ma);
			userOperationService.log(UserOperation.activityOperation(principal.getName(), ma.getCode(), 
					UserOperationType.ResumeMock));
		}
		return ma;
    }
	
	@RequestMapping(value = "/{code}/stop", method=RequestMethod.PUT)
    public MockActivity stop(@PathVariable String code, Principal principal) {
		MockActivity ma = updateStatus(code, principal.getName(), MockActivityStatus.Stopped);
		if (ma != null) {
			mockActivityService.clearRules(ma);
			userOperationService.log(UserOperation.activityOperation(principal.getName(), ma.getCode(), 
					UserOperationType.StopMock));
		}
		return ma;
    }
	
	private MockActivity updateStatus(String code, String owner, MockActivityStatus status) {
		return updateStatus(code, owner, status, null);
	}
	
	private MockActivity updateStatus(String code, String owner, MockActivityStatus status, List<Long> newMockerIds) {
		MockActivity ma = mockActivityDao.getMockActivityByCode(code);
		if (ma == null || !owner.equals(ma.getOwner())) {
			return null;
		} 
		ma.setStatus(status);
		if (newMockerIds != null) {
			ma.setMockerIds(newMockerIds);
		}
		return mockActivityDao.updateMockActivity(ma);
	}
}
