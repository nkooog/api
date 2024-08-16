package test.web.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.repository.MemberRepository;
import test.web.entity.user.Member;

import java.util.List;

@Slf4j
@Tag(name = "test", description = "Admin Test API")
@CrossOrigin(origins = "*", allowedHeaders = "*") // ??? 이거 뭐 어케 바꿔야함
@RestController
@RequestMapping(value = "/admin", produces = MediaTypes.HAL_JSON_VALUE)
public class AdminTestController {

	private final MemberRepository repository;

	private ObjectMapper objectMapper;
	public AdminTestController(MemberRepository repository, ObjectMapper objectMapper) {
		this.repository = repository;
		this.objectMapper = objectMapper;
	}

	@PostMapping("/test")
	public ResponseEntity test() throws Exception{
		log.debug(" admin 로그인 테스트 ");
		return ResponseEntity.ok().body("admin");
	}


	@PostMapping("/user/list")
	public ResponseEntity getUserList() throws Exception{

		List<Member> member = repository.findAll();

		return ResponseEntity.ok().body(this.objectMapper.writeValueAsString(member));
	}
}
