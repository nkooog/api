package test.web.controller.test;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.common.JsonViews;
import test.repository.MemberRepository;
import test.web.entity.user.Member;
import test.web.entity.user.MemberDTO;
import test.web.service.AdminService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "admin api", description = "Admin Test API")
@CrossOrigin(origins = "*", allowedHeaders = "*") // ??? 이거 뭐 어케 바꿔야함
@AllArgsConstructor
@RestController
@RequestMapping(value = "/admin", produces = MediaTypes.HAL_JSON_VALUE)
public class AdminTestController {

	private AdminService service;

	private ObjectMapper objectMapper;

	@PostMapping("/test")
	public ResponseEntity test() throws Exception{
		log.debug(" admin 로그인 테스트 ");
		return ResponseEntity.ok().body("admin");
	}


	@Operation(summary = "user list", description = "사용자 목록")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공",
					content = {@Content(schema = @Schema(implementation = MemberDTO.class))})
			,   @ApiResponse(responseCode = "400", description = "잘못 된 요청")
	})
	@JsonView(JsonViews.Hidden.class)
	@PostMapping("/user/list")
	public ResponseEntity getUserList() throws Exception{

		List<MemberDTO> memberList = service.getUserList();

		return ResponseEntity.ok().body(this.objectMapper.writeValueAsString(memberList));
	}
}
