package test.security.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import test.security.jwt.JwtAuthencationEntryPoint;
import test.security.jwt.JwtFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtFilter filter;
	private final JwtAuthencationEntryPoint entryPoint;
	private final String[] AUTH = {"/h2-console/**","/favicon.ico","/api/user/**"};

	public SecurityConfig(JwtFilter filter, JwtAuthencationEntryPoint entryPoint) {
		this.filter = filter;
		this.entryPoint = entryPoint;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.httpBasic(httpBasic -> httpBasic.disable())
				.csrf(csrfConfigurer -> csrfConfigurer.disable())
				.headers(headerConfig -> headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
				.authorizeHttpRequests(auth ->
						auth.requestMatchers(AUTH)
								.permitAll()
								.requestMatchers("/user/**").hasRole("USER").anyRequest().permitAll()
								.requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().permitAll()
				)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(exception -> exception.authenticationEntryPoint(entryPoint))
				.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

}
