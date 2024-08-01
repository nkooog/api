package test.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
public class JwtToken {

	private String grantType;
	private String accessToken;
	private String refreshToken;

}
