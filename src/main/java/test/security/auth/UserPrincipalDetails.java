package test.security.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import test.member.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipalDetails implements UserDetails {

	private final User user;

	public UserPrincipalDetails(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	// 계정의 권한을 담아두기위해
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(user.getRole()));
		return authorities;
	}

	// 계정의 비밀번호를 담아두기 위해
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	// 계정의 아이디를 담아두기 위해
	@Override
	public String getUsername() {
		return user.getName();
	}

	// 계정이 만료되지 않았는지를 담아두기 위해 (true: 만료안됨)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정이 잠겨있지 않았는지를 담아두기 위해 (true: 잠기지 않음)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 계정의 비밀번호가 만료되지 않았는지를 담아두기 위해 (true: 만료안됨)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 계정이 활성화되어있는지를 담아두기 위해 (true: 활성화됨)
	@Override
	public boolean isEnabled() {
		return true;
	}
}
