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

import javax.crypto.SecretKey;
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

	@Value("${jwt.header}")
	private String header;

	@Value("${jwt.type}")
	private String type;

	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public JwtToken generateToken(Authentication authentication) {
		// 권한 가져오기
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		Date accessTokenExpiration = getTokenDate(1);
		Date refreshTokenExpiration = getTokenDate(7);

		// Access Token 생성
		String accessToken = Jwts.builder()
				.subject(authentication.getName())
				.claim(this.header, authorities)
				.expiration(accessTokenExpiration)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();

		// Refresh Token 생성
		String refreshToken = Jwts.builder()
				.setExpiration(refreshTokenExpiration)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();

		return JwtToken.builder()
				.grantType(this.type.trim())
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
	}

	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parser().verifyWith(this.key).build().parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		} catch (SignatureException e) {
			return null;
		}
	}

	public Authentication getAuthentication(String accessToken) {
		// 토큰 복호화
		Claims claims = parseClaims(accessToken);

		if(claims == null) {System.out.println("is null");
			return null;
		}

		if (claims.get(this.header) == null) {
			throw new RuntimeException("권한 정보가 없는 토큰입니다.");
		}

		// 클레임에서 권한 정보 가져오기
		Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(claims.get(this.header).toString().split(","))
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());

		// UserDetails 객체를 만들어서 Authentication 리턴
		return new UsernamePasswordAuthenticationToken(null, null, authorities);
	}


	// LocalDateTime To Date
	public Date getTokenDate(Integer day) {
		return java.sql.Timestamp.valueOf(LocalDateTime.now().plusDays(day).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	}
}
