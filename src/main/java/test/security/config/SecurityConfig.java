package test.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import test.security.auth.UserPrincipalDetailsService;
import test.security.handler.UserAuthFailHandler;

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
				.csrf((csrfConfigurer) ->
						csrfConfigurer.disable()
				)
				.headers((headersConfig) ->
						headersConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable())
				)
				.authorizeHttpRequests(request ->
					request.requestMatchers("/h2-console/**", "/login").permitAll()
							.anyRequest().authenticated()
				)
				.formLogin(form ->
						form.loginPage("/login")
								.loginProcessingUrl("/main")
								.failureHandler(new UserAuthFailHandler())
								.permitAll()
					).logout( httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.logoutUrl("/logout").logoutSuccessUrl("/login?logout=1").deleteCookies("JSESSIONID"))
				.userDetailsService(userPrincipalDetailsService);

		return http.build();
	}

}
