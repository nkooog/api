package test.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import test.security.jwt.JwtAuthencationEntryPoint;
import test.security.jwt.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtFilter filter;
	private final JwtAuthencationEntryPoint entryPoint;
	private final String[] AUTH = {"/h2-console/**","/favicon.ico","/api/user/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger.html"};

	public SecurityConfig(JwtFilter filter, JwtAuthencationEntryPoint entryPoint) {
		this.filter = filter;
		this.entryPoint = entryPoint;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// form방식 로그인이 아니므로 비활성화
				.httpBasic(httpBasic -> httpBasic.disable())
				// csrf 비활성화 이유는 확인해야함
				.csrf(csrfConfigurer -> csrfConfigurer.disable())
				// h2 console을 사용하기 위함. 기본적으로 security에서 방지
				.headers(headerConfig -> headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
				.authorizeHttpRequests(auth ->
						auth.requestMatchers(AUTH)
								.permitAll()
								// DB에 ROLE_USER 형식이 아닌 USER 형식으로 들어가 있어 hasAuthority로 사용. 관례로 ROLE_USER 형식일 경우 hasRole로 사용
//								.requestMatchers("/user/**").hasRole("USER")
								.requestMatchers("/admin/**").hasAuthority("ADMIN")
								.requestMatchers("/user/**").hasAnyAuthority("ADMIN","USER")
				)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//				.exceptionHandling(exception -> exception.authenticationEntryPoint(entryPoint))
				.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

}
