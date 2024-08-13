package test.web.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "TB_USERS")
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Member implements UserDetails {

	@Id // 엔티티 내부에서 아이디임을 선언
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
	@Column(name = "MEMBER_ID") // 아이디에 해당하는 컬럼명 선언
	private Long id;

	@NotEmpty
	@Column(name = "MEMBER_LOGIN_ID")
	private String loginId;

	@Column(name = "MEMBER_ROLE")
	@Enumerated(EnumType.STRING)
	private MemberRole role = MemberRole.USER;

//	@NotEmpty
	@Column(name = "MEMBER_NAME")
	private String name;

	@Column(name = "MEMBER_PASSWORD")
	private String password;

//	@NotEmpty
	@Column(name = "MEMBER_EMAIL")
	private String email;

	@Column(name = "IS_USED")
	private String isUsed;

	@Column(name = "IS_DEL")
	private String isDel;

	@Column(name = "SYS_DATE")
	private LocalDateTime isrtDate;

	@Column(name = "REG_DATE")
	private LocalDateTime updtDate = LocalDateTime.now();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
}