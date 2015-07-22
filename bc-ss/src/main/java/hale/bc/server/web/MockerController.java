package hale.bc.server.web;

import hale.bc.server.repository.MockerDao;
import hale.bc.server.repository.exception.DuplicatedEntryException;
import hale.bc.server.service.UserOperationService;
import hale.bc.server.to.FailedResult;
import hale.bc.server.to.Mocker;
import hale.bc.server.to.UserOperation;
import hale.bc.server.to.UserOperationType;

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
		return mockerDao.getMockerById(mid, principal.getName());
    }
	
	@RequestMapping(method=RequestMethod.GET)
    public List<Mocker> getByOwner(Principal principal) {
		return mockerDao.getMockers(principal.getName());
    }
	
	@RequestMapping(value = "/public", method=RequestMethod.GET)
    public List<Mocker> getByPublic(Principal principal) {
		return mockerDao.getMockersByPublic(principal.getName());
    }
	
	@RequestMapping(value = "/{mid}/collect", method=RequestMethod.PUT)
    public String collect(@PathVariable Long mid, @RequestParam(value = "op", required = true) String operation, Principal principal) throws DuplicatedEntryException {
		Mocker m = mockerDao.getMockerById(mid);
		if (m == null) {
			return "{\"result\":-11, \"errorMsg\":\"该模拟器不存在！\"}";
		}
		String result = mockerDao.collectMockerById(mid, principal.getName());
		if ("1".equals(result)) {
			userOperationService.log(UserOperation.mockerOperation(principal.getName(), m, null, 
					UserOperationType.valueOf(operation)));
		}
		return "{\"result\":1, \"msg\":\"成功关注模拟系统:" + m.getName() + "！\"}";
    }
	
	@RequestMapping(value = "/{mid}/cancelCollect", method=RequestMethod.PUT)
    public String cancelCollect(@PathVariable Long mid, @RequestParam(value = "op", required = true) String operation, Principal principal) {
		Mocker m = mockerDao.getMockerById(mid);
		if (m == null) {
			return "{\"result\":-11, \"errorMsg\":\"该模拟器不存在！\"}";
		}
		String result = mockerDao.cancelCollectMockerById(mid, principal.getName());
		if ("1".equals(result)) {
			userOperationService.log(UserOperation.mockerOperation(principal.getName(), m, null, 
					UserOperationType.valueOf(operation)));
		}
		return "{\"result\":1, \"msg\":\"取消关注模拟系统:" + m.getName() + "！\"}";
    }
	
	@RequestMapping(value = "/collect", method=RequestMethod.GET)
    public List<Mocker> getCollect(Principal principal) {
		return mockerDao.getCollectMockers(principal.getName());
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
	
	@ExceptionHandler(DuplicatedEntryException.class)
	public FailedResult handleCustomException(DuplicatedEntryException ex) {
		return new FailedResult(-1, ex.getMessage());
	}
	
}
