package test.web.entity.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberDTO {

	@NotEmpty
	private String loginId;

//	@NotEmpty
	private String name;

	@NotEmpty
	private String password;

//	@NotEmpty
	private String email;

}
