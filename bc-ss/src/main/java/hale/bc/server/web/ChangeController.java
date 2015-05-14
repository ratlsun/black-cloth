package hale.bc.server.web;

import hale.bc.server.service.UserOperationService;
import hale.bc.server.to.UserChange;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/changes")
public class ChangeController {
	
	final static private long BEFORE = 7 * 24 * 3600 * 1000;	//one week
	
	@Autowired
	private UserOperationService userOperationService;
	
	@RequestMapping(method=RequestMethod.GET)
    public List<UserChange> getByOwner(Principal principal) {
		Date now = new Date();
		Date after = new Date(now.getTime() - BEFORE);
		return userOperationService.summary(after, principal.getName());
    }
	
}
