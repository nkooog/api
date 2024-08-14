package test.web.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import test.repository.MemberRepository;
import test.security.jwt.JwtToken;
import test.security.jwt.JwtTokenProvider;
import test.service.AuthenticationService;
import test.service.CustomUserDetailService;
import test.web.entity.user.Member;
import test.web.entity.user.MemberDTO;
import test.web.entity.user.MemberValidation;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*") // ??? 이거 뭐 어케 바꿔야함
@Tag(name = "Member", description = "Member API")
@RestController
@RequestMapping(value = "/api/user", produces = MediaTypes.HAL_JSON_VALUE)
public class MemberController {
	private final MemberValidation validation;
	private final MemberRepository repository;
	private final CustomUserDetailService userDetailService;
	private final JwtTokenProvider provider;
	private final AuthenticationService service;
	private final BCryptPasswordEncoder encoder;
	private ObjectMapper objectMapper;

	public MemberController(MemberValidation validation, MemberRepository repository, CustomUserDetailService userDetailService, JwtTokenProvider provider, ObjectMapper objectMapper, AuthenticationService service, BCryptPasswordEncoder encoder) {
		this.validation = validation;
		this.repository = repository;
		this.userDetailService = userDetailService;
		this.provider = provider;
		this.objectMapper = objectMapper;
		this.service = service;
		this.encoder = encoder;
	}

	@PostMapping(value = "/sign")
	public ResponseEntity userSign(@RequestBody @Valid MemberDTO memberDTO, Errors errors) throws Exception {

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
	public ResponseEntity login(@RequestBody MemberDTO memberDTO, Errors errors) {

		JwtToken token = null;
		Authentication authentication = service.authenticate(memberDTO.getLoginId(), memberDTO.getPassword(), errors);

		if(errors.hasErrors()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
		}

		token = provider.generateToken(authentication);

		return ResponseEntity.ok(token);
	}


}
