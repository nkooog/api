package test.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.SecurityFilterChain;
import test.security.auth.UserPrincipalDetailsService;
import test.security.handler.SecurityFailHandler;
import test.security.handler.SecuritySucessHandler;
import test.security.provider.UserAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserPrincipalDetailsService userPrincipalDetailsService;

	@Autowired
	private UserAuthenticationProvider provider;

	@Autowired
	public void configure (AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(provider);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			http
					.csrf(csrfConfigurer -> csrfConfigurer.disable())
					.headers(headerConfig -> headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
					.authorizeHttpRequests(auth ->
							auth.requestMatchers("/hello","/hello3","/h2-console/**","/login", "/css/**", "/js/**", "/images/**")
									.permitAll()
									.anyRequest().hasRole("USER"))
					.formLogin(form ->
							form.loginPage("/login/loginForm")
									.loginProcessingUrl("/login")
									.defaultSuccessUrl("/main")
									.successHandler(new SecuritySucessHandler())
									.failureHandler(new SecurityFailHandler())
									.permitAll()
					).userDetailsService(userPrincipalDetailsService);
		return http.build();
	}

}
