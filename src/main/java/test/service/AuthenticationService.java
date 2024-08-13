package test.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import test.web.entity.user.MemberValidation;

@Service
public class AuthenticationService {

	private final CustomUserDetailService userDetailsService;

	private final BCryptPasswordEncoder encoder;

	private final MemberValidation validation;

	public AuthenticationService(CustomUserDetailService userDetailsService, BCryptPasswordEncoder encoder, MemberValidation validation) {
		this.userDetailsService = userDetailsService;
		this.encoder = encoder;
		this.validation = validation;
	}

	public Authentication authenticate(String username, String password, Errors errors) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		if( userDetails == null || (userDetails != null && !encoder.matches(password, userDetails.getPassword()))) {
			validation.loginValidation(errors);
			return null;
		}else{
			return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
		}

	}

}
