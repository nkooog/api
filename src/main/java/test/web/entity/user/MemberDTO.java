package test.web.entity.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberDTO {

	@Schema(description = "회원 로그인 id")
	@NotEmpty
	private String loginId;

	@Schema(hidden = true)
//	@NotEmpty
	private String name;

	@Schema(description = "회원 패스워드")
	@NotEmpty
	private String password;

	@Schema(hidden = true)
//	@NotEmpty
	private String email;

}
