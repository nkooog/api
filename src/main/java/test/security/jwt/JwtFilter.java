package test.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {


	private final JwtTokenProvider provider;

	@Value("${jwt.header}")
	private String header;

	@Value("${jwt.type}")
	private String type;

	public JwtFilter(JwtTokenProvider provider) {
		this.provider = provider;
	}

	// http요청을 중간에 jwtFilter[커스터마이징 필터] 에서 토큰 검증
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		Function<HttpServletRequest, String> tokenResolver = this::resolveToken;
		String token = tokenResolver.apply(request);

		if( token != null ) {
			Authentication authentication = provider.getAuthentication(token);

			if(authentication!=null)  {
				// 다음 filter로 가기전 authentication 정보는 SecurityContextHolder에 담는다.
				SecurityContextHolder.getContext().setAuthentication(authentication);
				filterChain.doFilter(request, response);
			}
		}
		filterChain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(this.header);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(this.type)) {
			return bearerToken.substring(7);
		}
		return null;
	}

}
