package test.security.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import test.member.entity.User;
import test.security.auth.UserPrincipalDetails;
import test.security.auth.UserPrincipalDetailsService;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

	private Logger log = LoggerFactory.getLogger(getClass());

	private UserPrincipalDetailsService userPrincipalDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getName();
		String password = (String) authentication.getCredentials();

		log.debug(" >> " + username);
		log.debug(" >> " + password);

		UserPrincipalDetails userPrincipalDetails = (UserPrincipalDetails) userPrincipalDetailsService.loadUserByUsername(username);

		// db 에 저장된 password
		String dbPassword = userPrincipalDetails.getPassword();
		// 암호화 방식 (BCryptPasswordEncoder) 를 사용하여 비밀번호를 비교한다.
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		if(!passwordEncoder.matches(password, dbPassword)) {
			System.out.println("[사용자] 비밀번호가 일치하지 않습니다.");
			throw new BadCredentialsException("[사용자] 아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		// ========================================================================================

		// 사용자가 입력한 id 와 password 가 일치하면 인증이 성공한 것이다.
		// 인증이 성공하면 MemberPrincipalDetails 객체를 반환한다.
		User user = userPrincipalDetails.getUser();
		if (user == null || "N".equals(user.getIsUsed())) {
			System.out.println("[사용자] 사용할 수 없는 계정입니다.");
			throw new BadCredentialsException("[사용자] 사용할 수 없는 계정입니다.");
		}

		// 인증이 성공하면 UsernamePasswordAuthenticationToken 객체를 반환한다.
		// 해당 객체는 Authentication 객체로 추후 인증이 끝나고 SecurityContextHolder.getContext() 에 저장된다.
		return new UsernamePasswordAuthenticationToken(userPrincipalDetails, null, userPrincipalDetails.getAuthorities());

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return false;
	}
}
