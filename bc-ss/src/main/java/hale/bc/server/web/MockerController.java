package hale.bc.server.web;

import hale.bc.server.repository.MockerDao;
import hale.bc.server.repository.RuleDao;
import hale.bc.server.repository.exception.DuplicatedEntryException;
import hale.bc.server.service.UserOperationService;
import hale.bc.server.to.Mocker;
import hale.bc.server.to.UserOperation;
import hale.bc.server.to.UserOperationType;
import hale.bc.server.to.result.FailedResult;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/mockers")
public class MockerController {
	
	@Autowired
	private MockerDao mockerDao;
	
	@Autowired
	private RuleDao ruleDao;
	
	@Autowired
	private UserOperationService userOperationService;
	
	@RequestMapping(method=RequestMethod.POST)
    public Mocker add(@RequestBody Mocker mocker, Principal principal) throws DuplicatedEntryException {
		mocker.setOwner(principal.getName());
		Mocker nm = mockerDao.createMocker(mocker);
		if (nm != null) {
			userOperationService.log(UserOperation.mockerOperation(principal.getName(), mocker, nm, 
					UserOperationType.CreateMocker));
		}
		return nm;
    }
	
	@RequestMapping(value = "/{mid}", method=RequestMethod.GET)
    public Mocker getById(@PathVariable Long mid, Principal principal) {
		Mocker m = mockerDao.getMockerById(mid, principal.getName());
		if (m != null) {
			m.setRuleCount(ruleDao.getRuleCountByMocker(m.getId()));
		}
		return m;
    }
	
	@RequestMapping(method=RequestMethod.GET)
    public List<Mocker> getByOwner(Principal principal) {
		List<Mocker> ms = mockerDao.getMockers(principal.getName());
		for (Mocker m: ms) {
			m.setRuleCount(ruleDao.getRuleCountByMocker(m.getId()));
		}
		return ms;
    }
	
	@RequestMapping(value = "/{mid}", method=RequestMethod.PUT)
    public Mocker update(@RequestBody Mocker mocker, 
    			@RequestParam(value = "op", required = true) String operation, 
    			Principal principal)  throws DuplicatedEntryException {

		Mocker m = mockerDao.getMockerById(mocker.getId(), principal.getName());
		if (m == null) {
			return null;
		}
		Mocker nm = mockerDao.updateMocker(mocker);
		if (nm != null) {
			userOperationService.log(UserOperation.mockerOperation(principal.getName(), m, nm, 
					UserOperationType.valueOf(operation)));
		}
		return nm;
    }
	
	@RequestMapping(value = "/{mid}", method=RequestMethod.DELETE)
    public Mocker delete(@PathVariable Long mid, Principal principal) {
		Mocker m = mockerDao.getMockerById(mid, principal.getName());
		if (m == null) {
			return null;
		}
		Mocker nm =  mockerDao.deleteMocker(mid);
		if (nm != null) {
			userOperationService.log(UserOperation.mockerOperation(principal.getName(), m, nm, 
					UserOperationType.DeleteMocker));
		}
		return nm;
    }
	
	@RequestMapping(value = "/public", method=RequestMethod.GET)
    public List<Mocker> getByPublic(Principal principal) {
		List<Mocker> ms = mockerDao.getMockersByPublic(principal.getName());
		for (Mocker m: ms) {
			m.setRuleCount(ruleDao.getRuleCountByMocker(m.getId()));
		}
		return ms;
    }
	
	@RequestMapping(value = "/collect", method=RequestMethod.GET)
    public List<Mocker> getCollect(Principal principal) {
		List<Mocker> ms = mockerDao.getCollectMockers(principal.getName());
		for (Mocker m: ms) {
			m.setRuleCount(ruleDao.getRuleCountByMocker(m.getId()));
		}
		return ms;
    }
	
	@RequestMapping(value = "/{mid}/collect", method=RequestMethod.PUT)
    public Mocker collect(@PathVariable Long mid, @RequestParam(value = "op", required = true) String operation, Principal principal) throws DuplicatedEntryException {
		Mocker m = mockerDao.getMockerById(mid);
		if (m == null) {
			return null;
		}
		Mocker mocker = mockerDao.collectMockerById(mid, principal.getName());
		if (mocker != null) {
			userOperationService.log(UserOperation.mockerOperation(principal.getName(), m, null, 
					UserOperationType.valueOf(operation)));
		}
		return mocker;
    }
	
	@RequestMapping(value = "/{mid}/cancelCollect", method=RequestMethod.PUT)
    public Mocker cancelCollect(@PathVariable Long mid, @RequestParam(value = "op", required = true) String operation, Principal principal) {
		Mocker m = mockerDao.getMockerById(mid);
		if (m == null) {
			return null;
		}
		Mocker mocker = mockerDao.cancelCollectMockerById(mid, principal.getName());
		if (mocker != null) {
			userOperationService.log(UserOperation.mockerOperation(principal.getName(), m, null, 
					UserOperationType.valueOf(operation)));
		}
		return mocker;
    }
	
	@ExceptionHandler(DuplicatedEntryException.class)
	public FailedResult handleCustomException(DuplicatedEntryException ex) {
		return new FailedResult(-1, ex.getMessage());
	}
	
}
