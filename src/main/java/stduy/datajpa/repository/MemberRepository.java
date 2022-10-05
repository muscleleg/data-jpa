package stduy.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stduy.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUserNameAndAgeGreaterThan(String username, int age);

    List<Member> findHelloBy();
}
