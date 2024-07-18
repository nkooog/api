package test.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "TB_USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Long id;

	@Column(name = "USER_LOGIN_ID")
	private String userId;

	@Column(name = "USER_ROLE")
	private String role;

	@Column(name = "USER_NAME")
	private String name;

	@Column(name = "USER_PASSWD")
	private String password;

	@Column(name = "USER_EMAIL")
	private String email;

	@Column(name = "IS_USED")
	private String isUsed;

	@Column(name = "IS_DEL")
	private String isDel;

	@Column(name = "REG_DATE")
	private String regDate;

	@Column(name = "SYS_DATE")
	private String sysDate;

}
