package hale.bc.server.web;

import hale.bc.server.repository.UserDao;
import hale.bc.server.repository.exception.DuplicatedEntryException;
import hale.bc.server.repository.exception.ResetPwdLinkErrorException;
import hale.bc.server.service.exception.InvalidPasswordException;
import hale.bc.server.to.User;
import hale.bc.server.to.UserStatus;
import hale.bc.server.to.result.FailedResult;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@RequestMapping(method=RequestMethod.POST)
    public User add(@RequestBody User user) throws DuplicatedEntryException {
		user.setStatus(UserStatus.New);
		user.getRoles().add("USER");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userDao.createUser(user);
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{uid}", method=RequestMethod.GET, params="!by")
    public User get(@PathVariable Long uid)  {
		return userDao.getUserById(uid);
    }
	
	@PreAuthorize("(hasRole('ROLE_USER') and #n == authentication.name) or hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{name:.+}", method=RequestMethod.GET, params="by=name")
    public User getByName(@Param("n") @PathVariable String name)  {
		return userDao.getUserByName(name);
    }
	
	@RequestMapping(value = "/auth", method=RequestMethod.GET)
    public User getAuth(Principal principal)  {
		return userDao.getUserByName(principal.getName());
    }
	
	@RequestMapping(value = "/active", method=RequestMethod.PUT, params="code")
    public User activeByCode(@RequestParam(value = "code", required = true) String code)  {
		return userDao.activeUser(code);
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method=RequestMethod.GET)
    public List<User> getAll()  {
		return userDao.getAllUsers();
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{uid}/active", method=RequestMethod.PUT)
    public User active(@PathVariable Long uid)  {
		return userDao.updateUserStatus(uid, UserStatus.Active);
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{uid}/inactive", method=RequestMethod.PUT)
    public User inactive(@PathVariable Long uid)  {
		return userDao.updateUserStatus(uid, UserStatus.Inactive);
    }
	
	@RequestMapping(value = "/{uid}/changePwd", method=RequestMethod.PUT)
    public User eidtPassword(@RequestBody User user, @PathVariable Long uid, Principal principal)  throws InvalidPasswordException {
		User u = userDao.getUserById(uid);
		if (u == null || !u.getName().equals(principal.getName())) {
			return null;
		}
		if (passwordEncoder.matches(user.getPassword(), u.getPassword())) {
			u.setPassword(passwordEncoder.encode(user.getNewPwd()));
			return userDao.updateUser(u);
		} else {
			throw new InvalidPasswordException(user.getPassword());
		}
    }
	
	@ExceptionHandler(DuplicatedEntryException.class)
	public FailedResult handleCustomException(DuplicatedEntryException ex) {
		return new FailedResult(-1, ex.getMessage());
	}
	
	@ExceptionHandler(InvalidPasswordException.class)
	public FailedResult handleCustomException(InvalidPasswordException ex) {
		return new FailedResult(-11, ex.getMessage());
	}
	
	@ExceptionHandler(FileNotFoundException.class)
	public FailedResult handleCustomException(FileNotFoundException ex) {
		return new FailedResult(-12, "Config file not found.");
	}
	
	@ExceptionHandler(MessagingException.class)
	public FailedResult handleCustomException(MessagingException ex) {
		return new FailedResult(-13, "Mail send fail.");
	}
	
	@ExceptionHandler(ResetPwdLinkErrorException.class)
	public FailedResult handleCustomException(ResetPwdLinkErrorException ex) {
		return new FailedResult(-14, ex.getMessage());
	}
	
}
