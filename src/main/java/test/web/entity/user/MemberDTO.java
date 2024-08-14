package test.web.entity.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import test.common.JsonViews;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDTO {


	@Schema(description = "회원 로그인 id")
	@JsonView(JsonViews.Common.class)
	@NotEmpty
	private String loginId;

	@Schema(description = "회원명")
	@JsonView(JsonViews.Hidden.class)
//	@NotEmpty
	private String name;

	@Schema(description = "회원 패스워드")
	@JsonView(JsonViews.Common.class)
	@NotEmpty
	private String password;

	@Schema(description = "회원 이메일")
	@JsonView(JsonViews.Hidden.class)
//	@NotEmpty
	private String email;

}
