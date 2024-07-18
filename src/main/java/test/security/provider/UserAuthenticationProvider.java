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
