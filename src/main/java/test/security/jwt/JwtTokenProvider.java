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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

	private final SecretKey key;

	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}


	public JwtToken generateToken(Authentication authentication) {
		// 권한 가져오기
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		long now = (new Date()).getTime();

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


	//토큰으로부터 클레임을 만들고, 이를 통해 User 객체를 생성하여 Authentication 객체를 반환
	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);//accessToken에서 클레임을 가져온다.
		if (claims.get("auth") == null) {
			throw new RuntimeException("권한 정보가 담겨있지 않은 토큰입니다.");
		}

		Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(claims.get("auth").toString().split(","))
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());//사용자의 Role과 권한을 조회하여 SimpleAuthority 목록을 authorities에 세팅한다.

		UserDetails principal = new User(claims.getSubject(), "", authorities);//계정정보를 담은 User 객체를 생성한다.
		return new UsernamePasswordAuthenticationToken(principal, "", authorities);//Authentication 객체를 반환한다.
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
