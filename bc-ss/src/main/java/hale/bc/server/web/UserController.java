package hale.bc.server.web;

import hale.bc.server.repository.UserDao;
import hale.bc.server.repository.exception.DuplicatedEntryException;
import hale.bc.server.service.MailSenderService;
import hale.bc.server.service.UserService;
import hale.bc.server.service.exception.InvalidPasswordException;
import hale.bc.server.to.User;
import hale.bc.server.to.UserStatus;
import hale.bc.server.to.result.FailedResult;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${app.auto.sendInviteMail}")
    private boolean autoSendInviteMail;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MailSenderService mailService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@RequestMapping(method=RequestMethod.POST)
    public User add(@RequestBody User user) throws DuplicatedEntryException {
		user.setStatus(UserStatus.New);
		user.getRoles().add("USER");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User newUser = userDao.createUser(user);
		if (autoSendInviteMail){
			mailService.sendNewUserInviteMail(newUser);
		}
		return newUser;
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
	
	@RequestMapping(value = "/pcode", method=RequestMethod.GET, params="by=pcode")
    public User getByPasswordCode(@RequestParam(value = "code", required = true) String code)  {
		User user = userDao.getUserByPwdCode(code);
		if (user == null || userService.passwordResettingExpired(user)){
			return null;
		}
		return user;
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
    public User editPassword(@RequestBody User user, @PathVariable Long uid, Principal principal)  throws InvalidPasswordException {
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
	
	@RequestMapping(value = "/{name:.+}/applyPwd", method=RequestMethod.PUT)
    public User applyResetPassword(@PathVariable String name) {
		User u = userDao.getUserByName(name);
		if (u == null) {
			return null;
		}
		u = userDao.attachPwdCodeToUser(u);
		mailService.sendPasswordResettingMail(u);
		return u;
    }
	
	@RequestMapping(value = "/{uid}/resetPwd", method=RequestMethod.PUT)
    public User resetPwd(@RequestBody User user, @PathVariable Long uid) {
		User u = userDao.getUserByPwdCode(user.getPwdCode());
		if (u == null || userService.passwordResettingExpired(u)){
			return null;
		}
		u.setPassword(passwordEncoder.encode(user.getNewPwd()));
		return userDao.releasePwdCodeAndUpdateUser(u);
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{uid}/sendCode", method=RequestMethod.PUT)
    public User sendCode(@PathVariable Long uid)  {
		User user = userDao.getUserById(uid);
		if (user == null || user.getCode() == null || user.getCode().isEmpty()) {
			return null;
		}
		mailService.sendNewUserInviteMail(user);
		return user;
    }
	
	@ExceptionHandler(DuplicatedEntryException.class)
	public FailedResult handleCustomException(DuplicatedEntryException ex) {
		return new FailedResult(-1, ex.getMessage());
	}
	
	@ExceptionHandler(InvalidPasswordException.class)
	public FailedResult handleCustomException(InvalidPasswordException ex) {
		return new FailedResult(-11, ex.getMessage());
	}
}
