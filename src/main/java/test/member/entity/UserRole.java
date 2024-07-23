package test.member.entity;

public enum UserRole {

		USER_ROLE("일반사용자", "USER")
	,   USER_ADMIN("일반관리자", "ADMIN");

	private String value;
	private String role;

	public String getValue() {
		return value;
	}

	public String getRole() {
		return role;
	}

	UserRole(String value, String role) {
		this.value = value;
		this.role = role;
	}
}
