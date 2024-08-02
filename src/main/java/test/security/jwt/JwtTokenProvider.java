package test.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

	private final Key key;

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

	// LocalDateTime To Date
	public Date getTokenDate(Integer day) {
		return java.sql.Timestamp.valueOf(LocalDateTime.now().plusDays(day).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	}

}
