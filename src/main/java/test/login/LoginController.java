package test.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	private Logger log = LoggerFactory.getLogger(getClass());

	@GetMapping("/login")
	public String login() throws Exception {
		System.out.println(" ######## loginForm");
		return "login/loginForm";
	}

	@RequestMapping("/hello")
	public String hello() throws Exception {
		System.out.println(" ######## hello");
		return "hello/hello";
	}

	@RequestMapping("/hello4")
	public String hello4() throws Exception {
		System.out.println(" ######## hello");
		return "hello4/hello4";
	}

}
