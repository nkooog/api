package test.web.controller.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import test.common.JsonViews;
import test.repository.MemberRepository;
import test.security.jwt.JwtToken;
import test.security.jwt.JwtTokenProvider;
import test.service.AuthenticationService;
import test.web.entity.user.Member;
import test.web.entity.user.MemberDTO;
import test.web.entity.user.MemberValidation;
import test.web.service.UserService;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*") // ??? 이거 뭐 어케 바꿔야함
@Tag(name = "Member", description = "Member API")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/user", produces = MediaTypes.HAL_JSON_VALUE)
public class MemberController {

	private final MemberValidation validation;
	private final MemberRepository repository;
	private final JwtTokenProvider provider;
	private final AuthenticationService service;
	private ObjectMapper objectMapper;

	@Operation(summary = "user sign up", description = "사용자 가입")
	@ApiResponses(value = {
				@ApiResponse(responseCode = "201", description = "성공",
					content = {@Content(schema = @Schema(implementation = MemberDTO.class))})
			,   @ApiResponse(responseCode = "400", description = "잘못 된 요청")
	})
	@JsonView(JsonViews.Hidden.class)
	@PostMapping(value = "/sign")
	public ResponseEntity userSign(@RequestBody MemberDTO memberDTO, Errors errors) throws Exception {

		if(errors.hasErrors()) {
			return ResponseEntity.badRequest().body(errors);
		}

		validation.validate(memberDTO, errors);
		if(errors.hasErrors()) {
			return ResponseEntity.badRequest().body(errors);
		}

		Member member = this.objectMapper.convertValue(memberDTO, Member.class);
		Member memberNew = this.repository.save(member);

		URI createURI = linkTo(getClass()).slash(memberNew.getId()).toUri();
		return ResponseEntity.created(createURI).body(member);
	}

	@Operation(summary = "login", description = "사용자 로그인")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공",
					content = {@Content(schema = @Schema(implementation = JwtToken.class))}),
			@ApiResponse(responseCode = "404", description = "회원정보를 확인해주세요."),
	})
	@Parameter(name="loginId, password", description = "로그인 유저 ID, 패스워드", example = "member1", required = true)
	@PostMapping("/login")
	@JsonView(JsonViews.Common.class)
	public ResponseEntity login(@RequestBody MemberDTO memberDTO, Errors errors) throws JsonProcessingException {

		Authentication authentication = service.authenticate(memberDTO.getLoginId(), memberDTO.getPassword(), errors);

		if(errors.hasErrors()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
		}

		JwtToken token = provider.generateToken(authentication);

		return ResponseEntity.ok().body(this.objectMapper.writeValueAsString(token));
	}


}
