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

		// DB정보와 입력한 사용자정보가 일치하는 경우 UserDetail 객체 리턴
		User user = this.repository.findByUserId(username);

		// 일치하지 않을경우 처리
		if(user == null) throw new UsernameNotFoundException("");

		return new UserPrincipalDetails(user);
	}
}
