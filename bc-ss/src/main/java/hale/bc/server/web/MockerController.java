package hale.bc.server.web;

import hale.bc.server.repository.MockerDao;
import hale.bc.server.repository.exception.DuplicatedEntryException;
import hale.bc.server.to.FailedResult;
import hale.bc.server.to.Mocker;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
	
	@PreAuthorize("hasRole('ROLE_USER')")
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
		return mockerDao.updateMocker(mocker, principal.getName());
    }
	
	@RequestMapping(value = "/{mid}", method=RequestMethod.DELETE)
    public Mocker delete(@PathVariable Long mid, Principal principal) {
		return mockerDao.deleteMocker(mid, principal.getName());
    }
	
	@ExceptionHandler(DuplicatedEntryException.class)
	public FailedResult handleCustomException(DuplicatedEntryException ex) {
		return new FailedResult(-1, ex.getMessage());
	}
	
}
