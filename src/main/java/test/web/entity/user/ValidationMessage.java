package test.web.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor @NoArgsConstructor
public enum ValidationMessage {
	EXISTS("loginId", "exists", "해당 로그인 아이디는 이미 등록되어 있습니다. 다른 아이디를 시도해보세요."),
	PASSWORD("password","length error","비밀번호는 최소 8자 이상이어야 합니다."),
	NOT_MATCH_PASSWORD("password", "password not match", "패스워드를 다시 입력해주세요.");

	private String field;
	private String error;
	private String message;

}
