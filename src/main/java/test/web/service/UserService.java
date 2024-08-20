package test.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.stereotype.Service;
import test.repository.MemberRepository;
import test.web.entity.user.Member;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

	private final MemberRepository repository;

	private ObjectMapper objectMapper;
	public Optional<Member> getUser(Long id) {
		Optional<Member> member = this.repository.findByMemberDetail(id);
		log.debug(ToStringBuilder.reflectionToString(member));
		return member;
	}

}
