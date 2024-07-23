package test.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

	private Logger log = LoggerFactory.getLogger(getClass());


	@RequestMapping("/login/loginForm")
	public String loginForm() throws Exception {
		System.out.println(" ######## loginForm");
		return "login/loginForm";
	}

}
