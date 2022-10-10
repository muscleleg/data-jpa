package stduy.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stduy.datajpa.dto.MemberDto;
import stduy.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findHelloBy();

    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findByUsernameList();

    @Query("select new stduy.datajpa.dto.MemberDto(m.id,m.username, t.name) from Member m join m .team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
//    List<Member> findByUsername(@Param("names") List<String> names);
    List<Member> findByUsername(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); //컬랙션
    Member findMemberByUsername(String username); //단건
//    List<Member> findMemberByUsername(String username); //단건
    Optional<Member> findOptionalByUsername(String username);//단건 Optional

//    Page<Member> findByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m where m.age =:age ORDER BY m.username desc",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(@Param("age") int age,Pageable pageable);

    List<Member> findTop3ByAge(int age);
}
