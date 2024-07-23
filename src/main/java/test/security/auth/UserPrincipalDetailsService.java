package test.security.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import test.member.entity.User;
import test.member.repository.UserRepository;

@Component
public class UserPrincipalDetailsService implements UserDetailsService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private UserRepository repository;

	public UserPrincipalDetailsService(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


		User user = repository.findByUserId(username);

		if(user == null) System.out.println("user is null");

		log.debug(" >>> " + user.getPassword()) ;

		return new UserPrincipalDetails(user);
	}
}
