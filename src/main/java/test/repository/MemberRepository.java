package test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import test.web.entity.user.Member;
import test.web.entity.user.MemberDTO;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByLoginId(String loginId);
	boolean existsByLoginId(String loginId);
	Optional<Member> findById(@Param("id") Long id);
}
