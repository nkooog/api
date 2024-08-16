package test;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.With;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import test.web.entity.user.Member;
import test.web.entity.user.MemberDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class Demo2ApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	public void test11() throws Exception {
		Member member = Member.builder()
				.loginId("member1")
				.password("123456789")
				.email("test@test.com")
				.name("테스트사용자").build();
		mockMvc.perform(post("/api/user/sign")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaTypes.HAL_JSON_VALUE)
				.content(objectMapper.writeValueAsString(member))
		)
				.andExpect(status().isCreated())
				.andDo(print());
	}

	@Test
	public void 로그인및토큰발급테스트() throws Exception {

		MemberDTO memberDTO = MemberDTO.builder()
				.loginId("member1")
				.password("test")
				.build();

		mockMvc.perform(post("/api/user/login")
				.contentType(MediaTypes.HAL_JSON_VALUE)
				.accept(MediaTypes.HAL_JSON_VALUE)
				.content(this.objectMapper.writeValueAsString(memberDTO))
		)
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void test() {
		long now = (new Date()).getTime();

		// 2. LocalDateTime -> Date 변환
		Date date = java.sql.Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		Date exp = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		System.out.println(date);
		System.out.println(exp);

	}

	@Test
	public void test1() {
		String requestURL = "/user/sign1";
		String[] permitAllArray= {"/user/sign","/h2-console","/api/token"};
		boolean result = Arrays.stream(permitAllArray).anyMatch(
				e -> e.equals(requestURL)
		);

		Arrays.stream(permitAll.values()).forEach(e -> System.out.println(e));

		System.out.println(Arrays.toString(permitAll.values()));
	}

	@Getter
	enum permitAll {
		USER_SIGN("/user/sign"),
		H2_CONSOLE("/h2-console/**"),
		FAVICON("/favicon.ico");

		private String url;
		permitAll(String url) {
			this.url = url;
		}
	}

	@Test
	public void secret() throws Exception {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println(passwordEncoder.encode("test"));
	}

	@Test
	@WithMockUser(username = "admin1", roles = {"ADMIN"})
	public void 관리자테스트() throws Exception {

		String token ="Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEiLCJBdXRob3JpemF0aW9uIjoiQURNSU4iLCJleHAiOjE3MjM4NzQ4OTh9.sCkGAvypw7fFVLk-u5gYVx7kcrvHxaWNQRcCpVCudx4";

		mockMvc.perform(post("/user/test")
				.header("Authorization", token)
				.contentType(MediaTypes.HAL_JSON_VALUE)
				.accept(MediaTypes.HAL_JSON_VALUE)
		)
				.andExpect(status().isOk())
				.andDo(print());
	}

}
