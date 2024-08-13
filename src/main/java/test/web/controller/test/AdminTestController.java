package test.web.controller.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*") // ??? 이거 뭐 어케 바꿔야함
@RestController
@RequestMapping(value = "/admin", produces = MediaTypes.HAL_JSON_VALUE)
public class AdminTestController {

	@PostMapping("/test")
	public ResponseEntity test() throws Exception{
		log.debug(" admin 로그인 테스트 ");
		return ResponseEntity.ok().body("admin");
	}
}
