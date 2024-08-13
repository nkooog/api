package test.web.entity.user;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import test.repository.MemberRepository;

@Component
public class MemberValidation {

	private final MemberRepository repository;


	public MemberValidation(MemberRepository repository) {
		this.repository = repository;
	}

	public void validate(MemberDTO memberDTO, Errors errors) {

		boolean exists = repository.existsByLoginId(memberDTO.getLoginId());

		if(exists) {
			errors.rejectValue(ValidationMessage.EXISTS.getField(), ValidationMessage.EXISTS.getError(), ValidationMessage.EXISTS.getMessage());
		}

	}

	public void loginValidation(Errors errors) {
		errors.rejectValue(ValidationMessage.NOT_MATCH.getField(), ValidationMessage.NOT_MATCH.getError(), ValidationMessage.NOT_MATCH.getMessage());
	}

}
