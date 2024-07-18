package test.security.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import test.member.entity.User;
import test.member.repository.UserRepository;

@Component
public class UserPrincipalDetailsService implements UserDetailsService {

	private UserRepository repository;
	public UserPrincipalDetailsService(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = this.repository.findByUserId(username);

		if(user == null) throw new UsernameNotFoundException("");

		return new UserPrincipalDetails(user);
	}
}
