package hale.bc.server.web;

import hale.bc.server.repository.MockActivityDao;
import hale.bc.server.repository.MockHitDao;
import hale.bc.server.to.MockActivity;
import hale.bc.server.to.MockHit;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/mock-hits")
public class MockHitController {
	
	final static private int LATEST_COUNT = 20;	
	
	@Autowired
	private MockHitDao mockHitDao;
	
	@Autowired
	private MockActivityDao mockActivityDao;
	
	@RequestMapping(method=RequestMethod.GET, params="acode")
    public List<MockHit> getByActivityCode(@RequestParam(value = "acode", required = true) String code, Principal principal) {
		MockActivity ma = mockActivityDao.getMockActivityByCode(code);
		if (ma == null || !ma.getOwner().equals(principal.getName())){
			return null;
		}
		return mockHitDao.getMockHitsByActivityCode(code, LATEST_COUNT);
    }
	
}
