package test.web.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
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

	@PostMapping("/login")
	public ResponseEntity login(@RequestBody MemberDTO memberDTO, Errors errors) {

		log.debug(" ##### login");
		JwtToken token = null;
		Authentication authentication = service.authenticate(memberDTO.getLoginId(), memberDTO.getPassword(), errors);

		if(errors.hasErrors()) {
			return ResponseEntity.badRequest().body(errors);
		}

		token = provider.generateToken(authentication);


		return ResponseEntity.ok(token);
	}


}
