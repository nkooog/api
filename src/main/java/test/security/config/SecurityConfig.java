package test.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import test.security.auth.UserPrincipalDetailsService;
import test.security.handler.UserAuthFailHandler;
import test.security.handler.UserAuthSucessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private UserPrincipalDetailsService userPrincipalDetailsService;

	public SecurityConfig(UserPrincipalDetailsService userPrincipalDetailsService) {
		this.userPrincipalDetailsService = userPrincipalDetailsService;
	}

		@Bean
		public WebSecurityCustomizer webSecurityCustomizer(){
			return web -> web.ignoring()
					.requestMatchers("/css/**", "/js/**", "/images/**");
		}

		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			http
					.csrf(csrfConfigurer -> csrfConfigurer.ignoringRequestMatchers("/h2-console/**").disable())
				.headers(headerConfig -> headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable())
				)
				.authorizeHttpRequests(auth ->
							auth.requestMatchers("/login", "/join")
						)
				.formLogin(form ->
							form.loginPage("/login")
									.loginProcessingUrl("/user/login")
									.defaultSuccessUrl("/user/main")
									.successHandler(new UserAuthSucessHandler())
									.failureHandler(new UserAuthFailHandler())
									.permitAll()
						).logout(
								logout -> logout.logoutUrl("/logout")
												.logoutSuccessUrl("/login?logout=1")
												.deleteCookies("JSESSIONID")
				);
		return http.build();
	}

}
