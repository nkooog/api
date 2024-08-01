package test.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

	// http요청을 중간에 jwtFilter[커스터마이징 필터] 에서 토큰 검증
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String authorizationHeader = request.getHeader("Authorization");

		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			// 토큰 인증 검사
			System.out.println(" #### ");
		}else{
			// error handler
			System.out.println(" @@@@ ");
		}

		// 다음 필터로 넘기기
		filterChain.doFilter(request, response);
	}
}
