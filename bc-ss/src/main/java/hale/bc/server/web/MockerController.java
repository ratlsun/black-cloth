package hale.bc.server.web;

import hale.bc.server.repository.MockerDao;
import hale.bc.server.repository.exception.DuplicatedEntryException;
import hale.bc.server.to.FailedResult;
import hale.bc.server.to.Mocker;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/mockers")
public class MockerController {
	
	@Autowired
	private MockerDao mockerDao;
	
	@RequestMapping(method=RequestMethod.POST)
    public Mocker add(@RequestBody Mocker mocker, Principal principal) throws DuplicatedEntryException {
		mocker.setOwner(principal.getName());
		return mockerDao.createMocker(mocker);
    }
	
	@RequestMapping(value = "/{mid}", method=RequestMethod.GET)
    public Mocker getById(@PathVariable Long mid, Principal principal) {
		return mockerDao.getMockerById(mid, principal.getName());
    }
	
	@RequestMapping(method=RequestMethod.GET)
    public List<Mocker> getByOwner(Principal principal) {
		return mockerDao.getMockers(principal.getName());
    }
	
	@RequestMapping(value = "/{mid}", method=RequestMethod.PUT)
    public Mocker update(@RequestBody Mocker mocker, Principal principal)  throws DuplicatedEntryException {
		Mocker m = mockerDao.getMockerById(mocker.getId(), principal.getName());
		if (m == null) {
			return null;
		}
		return mockerDao.updateMocker(mocker);
    }
	
	@RequestMapping(value = "/{mid}", method=RequestMethod.DELETE)
    public Mocker delete(@PathVariable Long mid, Principal principal) {
		Mocker m = mockerDao.getMockerById(mid, principal.getName());
		if (m == null) {
			return null;
		}
		return mockerDao.deleteMocker(mid);
    }
	
	@ExceptionHandler(DuplicatedEntryException.class)
	public FailedResult handleCustomException(DuplicatedEntryException ex) {
		return new FailedResult(-1, ex.getMessage());
	}
	
}
