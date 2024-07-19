package test.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import test.security.auth.UserPrincipalDetails;

public class UserAuthenticationProvider implements AuthenticationProvider {

	private UserPrincipalDetails details;

	public UserAuthenticationProvider(UserPrincipalDetails details) {
		this.details = details;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		//AuthenticationManager에게 위임받아 사용자 정보를 인증하고 인증객체를 만들어줌

		//DB에 있는 사용자정보와 form을 통해 입력한 사용자 정보 매칭 인증

		return new UsernamePasswordAuthenticationToken(details
				,null
				,details.getAuthorities()
				 );
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
