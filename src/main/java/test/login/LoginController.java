package test.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import test.security.auth.UserPrincipalDetails;

@Controller
public class LoginController {

	private Logger log = LoggerFactory.getLogger(getClass());

	private boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.debug("authentication :" + authentication);
		return (authentication == null ||
				!(AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())))
				? false : authentication.isAuthenticated();
	}

	@RequestMapping("/test")
	public String test() {
		return "test";
	}

	@RequestMapping("/login")
	public String loginForm(HttpServletRequest request, @AuthenticationPrincipal UserPrincipalDetails userPrincipalDetails) throws Exception {
		System.out.println(" ######## loginForm");
		return "login/loginForm";
	}

	@RequestMapping("/main")
	public String main() {
		return"main/main";
	}

}
