package test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.login.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	Member findByLoginId(String username);

}
