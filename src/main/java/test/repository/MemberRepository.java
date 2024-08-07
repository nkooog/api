package test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.web.entity.user.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	Member findByLoginId(String loginId);

	boolean existsByLoginId(String loginId);

}
