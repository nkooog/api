package test.web.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import test.web.entity.user.Member;
import test.web.entity.user.MemberDTO;
import test.web.service.UserService;

import java.util.Optional;

@Slf4j
@Tag(name = "test", description = "User Test API")
@CrossOrigin(origins = "*", allowedHeaders = "*") // ??? 이거 뭐 어케 바꿔야함
@AllArgsConstructor
@RestController
@RequestMapping(value = "/user", produces = MediaTypes.HAL_JSON_VALUE)
public class UserTestController {

	private UserService service;
	private ObjectMapper objectMapper;
	@PostMapping("/test")
	public ResponseEntity test() throws Exception{
		log.debug(" user 로그인 테스트 ");
		return ResponseEntity.ok().body("test");
	}

	@GetMapping("/{id}")
	public ResponseEntity getUser(@PathVariable Long id) throws  Exception {
		Optional<Member> member = this.service.getUser(id);
		return ResponseEntity.ok().body(this.objectMapper.writeValueAsString(member));
	}

}
