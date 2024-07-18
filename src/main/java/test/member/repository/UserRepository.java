package test.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.member.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserId(String username);

}
