package test.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import test.repository.MemberRepository;
import test.web.entity.user.Member;
import test.web.entity.user.MemberDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminService {

	private final MemberRepository repository;

	private ObjectMapper objectMapper;

	public List<MemberDTO> getUserList() {
		List<Member> list = repository.findAll();
		return list.stream()
				.map(member -> this.objectMapper.convertValue(member, MemberDTO.class))
				.collect(Collectors.toList());
	}

}
