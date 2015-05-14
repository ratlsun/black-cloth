package hale.bc.server.web;

import hale.bc.server.repository.MockerDao;
import hale.bc.server.repository.RuleDao;
import hale.bc.server.service.UserOperationService;
import hale.bc.server.to.Mocker;
import hale.bc.server.to.Rule;
import hale.bc.server.to.UserOperation;
import hale.bc.server.to.UserOperationType;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/rules")
public class RuleController {
	
	@Autowired
	private RuleDao ruleDao;
	
	@Autowired
	private MockerDao mockerDao;
	
	@Autowired
	private UserOperationService userOperationService;
	
	@RequestMapping(method=RequestMethod.POST)
    public Rule add(@RequestBody Rule rule, Principal principal) {
		Mocker m = mockerDao.getMockerById(rule.getMockerId(), principal.getName());
		if (m == null) {
			return null;
		}
		Rule nr = ruleDao.createRule(rule);
		if (nr != null) {
			userOperationService.log(UserOperation.ruleOperation(principal.getName(), rule.getId(), m, 
					UserOperationType.CreateRule));
		}
		return nr;
    }
	
	@RequestMapping(method=RequestMethod.GET)
    public List<Rule> getByMock(@RequestParam(value = "mid", required = true) Long mockerId, Principal principal) {
		if (mockerDao.getMockerById(mockerId, principal.getName()) == null) {
			return null;
		}
		return ruleDao.getRulesByMocker(mockerId);
    }
	
	@RequestMapping(value = "/{rid}", method=RequestMethod.GET)
    public Rule getById(@PathVariable Long rid, Principal principal) {
		Rule r = ruleDao.getRuleById(rid);
		if (mockerDao.getMockerById(r.getMockerId(), principal.getName()) == null) {
			return null;
		}
		return r;
    }
	
	@RequestMapping(value = "/{rid}", method=RequestMethod.PUT)
    public Rule update(@RequestBody Rule rule, Principal principal)  {
		Rule r = ruleDao.getRuleById(rule.getId());
		if (r == null) {
			return null;
		}
		Mocker m = mockerDao.getMockerById(r.getMockerId(), principal.getName());
		if (m == null) {
			return null;
		}
		Rule nr = ruleDao.updateRule(rule);
		if (nr != null) {
			userOperationService.log(UserOperation.ruleOperation(principal.getName(), rule.getId(), m, 
					UserOperationType.UpdateRule));
		}
		return nr;
    }
	
	@RequestMapping(value = "/{rid}", method=RequestMethod.DELETE)
    public Rule delete(@PathVariable Long rid, Principal principal) {
		Rule r = ruleDao.getRuleById(rid);
		if (r == null) {
			return null;
		}
		Mocker m = mockerDao.getMockerById(r.getMockerId(), principal.getName());
		if (m == null) {
			return null;
		}
		Rule nr = ruleDao.deleteRule(rid);
		if (nr != null) {
			userOperationService.log(UserOperation.ruleOperation(principal.getName(), rid, m, 
					UserOperationType.DeleteRule));
		}
		return nr;
    }
	
}
