package test.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private final String[] permitAllArray= {"/user/sign","/api/token", "/h2-console/login.do", "/h2-console/login.jsp", "/h2-console/"};
	// http요청을 중간에 jwtFilter[커스터마이징 필터] 에서 토큰 검증
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		filterChain.doFilter(request, response);

		// token 검증

	}

	private boolean getPermitAllRequest(String requestURI) {
		return Arrays.stream(permitAllArray).anyMatch(
				e -> e.equals(requestURI)
		);
	}

}
