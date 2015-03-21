package hale.bc.server.service;

import hale.bc.server.repository.UserDao;
import hale.bc.server.to.User;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = userDao.getUserByName(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		List<SimpleGrantedAuthority> roles = new ArrayList<>();
		for (String r : user.getRoles()){
			roles.add(new SimpleGrantedAuthority("ROLE_" + r));
		}
		return new org.springframework.security.core.userdetails.User(
				user.getName(), user.getPassword(), user.isActive(), true, true, true, roles);
	}
	
}
