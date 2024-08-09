package test.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	private final CustomUserDetailService userDetailsService;

	private final BCryptPasswordEncoder encoder;

	public AuthenticationService(CustomUserDetailService userDetailsService, BCryptPasswordEncoder encoder) {
		this.userDetailsService = userDetailsService;
		this.encoder = encoder;
	}

	public Authentication authenticate(String username, String password) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		if (encoder.matches(password, userDetails.getPassword())) {
			return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
		} else {
			throw new BadCredentialsException("Invalid username or password");
		}
	}

}
