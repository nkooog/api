package test.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import test.repository.MemberRepository;
import test.web.entity.user.Member;

import java.util.Collections;

@Service
public class CustomUserDetailService implements UserDetailsService {

	private final MemberRepository repository;

	public CustomUserDetailService(MemberRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findByLoginId(username)
				.map(this::createUserDetails)
				.orElseThrow(() -> null);
	}

	// DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
	private UserDetails createUserDetails(Member member) {
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().toString());

		return new User(
				member.getLoginId(),
				member.getPassword(),
				Collections.singleton(grantedAuthority)
		);
	}
}
