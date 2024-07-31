package test.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import test.security.jwt.JwtAuthencationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthencationEntryPoint entryPoint;

	public SecurityConfig(JwtAuthencationEntryPoint entryPoint) {
		this.entryPoint = entryPoint;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.httpBasic(httpBasic -> httpBasic.disable())
				.csrf(csrfConfigurer -> csrfConfigurer.disable())
				.headers(headerConfig -> headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
				.authorizeHttpRequests(auth ->
						auth.requestMatchers("/hello","/hello3","/h2-console/**","/login")
								.permitAll()
								.anyRequest().hasRole("USER"))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(exceptionHalder->exceptionHalder.authenticationEntryPoint(entryPoint));
		return http.build();
	}

}
