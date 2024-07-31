package test.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

	private final Key key;

	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}


	public String createAccessToken(String id, String role) {

		String accessToken = Jwts.builder()
				.claim("id", id)
				.claim("role", role)
				.issuedAt(new Date(System.currentTimeMillis()))
				.signWith(this.key)
				.expiration(new Date(System.currentTimeMillis())) // 만료시간 설정
				.compact();

		return accessToken;
	}
}
