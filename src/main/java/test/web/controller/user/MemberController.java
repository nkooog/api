package test.web.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import test.repository.MemberRepository;
import test.web.entity.user.Member;
import test.web.entity.user.MemberDTO;
import test.web.entity.user.MemberValidation;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RestController
@RequestMapping(value = "/api/user", produces = MediaTypes.HAL_JSON_VALUE)
public class MemberController {
	private final MemberValidation validation;
	private final MemberRepository repository;
	private ObjectMapper objectMapper;

	public MemberController(MemberValidation validation, MemberRepository repository, ObjectMapper objectMapper) {
		this.validation = validation;
		this.repository = repository;
		this.objectMapper = objectMapper;
	}

	@GetMapping(value="/{id}")
	public ResponseEntity findByMember(@PathVariable String id) throws Exception {
		Member member = this.repository.findByLoginId(id);

		if(member == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().body(member);
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



}
