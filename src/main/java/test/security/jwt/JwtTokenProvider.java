package test.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import test.repository.MemberRepository;
import test.web.entity.user.Member;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

	private final SecretKey key;

	private final MemberRepository repository;

	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, MemberRepository repository) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.repository = repository;
	}

	public JwtToken generateToken(Authentication authentication) {
		// 권한 가져오기
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		long now = (new Date().getTime());

		Date accessTokenExpiration = getTokenDate(1);
		Date refreshTokenExpiration = getTokenDate(7);

		// Access Token 생성
		String accessToken = Jwts.builder()
				.subject(authentication.getName())
				.claim("auth", authorities)
				.expiration(accessTokenExpiration)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();

		// Refresh Token 생성
		String refreshToken = Jwts.builder()
				.setExpiration(refreshTokenExpiration)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();

		return JwtToken.builder()
				.grantType("Bearer")
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
	}

	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parser().verifyWith(this.key).build().parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	// 토큰 정보를 검증하는 메서드
	public boolean validateToken(String token) {
		System.out.println(token);
		try {
			Jwts.parser()
					.verifyWith(this.key)
					.build()
					.parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT Token", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty.", e);
		}
		return false;
	}


	// LocalDateTime To Date
	public Date getTokenDate(Integer day) {
		return java.sql.Timestamp.valueOf(LocalDateTime.now().plusDays(day).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	}
}
