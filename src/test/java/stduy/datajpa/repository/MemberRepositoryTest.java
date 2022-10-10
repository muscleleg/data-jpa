package stduy.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import stduy.datajpa.dto.MemberDto;
import stduy.datajpa.entity.Member;
import stduy.datajpa.entity.Team;

import javax.persistence.NonUniqueResultException;
import java.awt.print.Pageable;
import java.lang.reflect.Array;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @Test
    public void testMember() {
        System.out.println("memberRepository.getClass() = " + memberRepository.getClass());

        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);

    }
    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);
//
//        //수정
//        findMember1.setUserName("member!!!!");
        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }
    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }
    @Test
    public void findHelloBy() {
        List<Member> helloBy = memberRepository.findHelloBy();
    }
    @Test
    public void namedQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA",10);
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }
    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findByUsernameList();
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }
    @Test
    public void findMemberDto() {
        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);

        Team team = new Team("TeamA");
        m1.setTeam(team);
        teamRepository.save(team);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void findNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

//        List<String> strings = new ArrayList<>();
//        strings.add(m1.getUsername());
//        strings.add(m2.getUsername());
//        List<Member> result = memberRepository.findByUsername(strings);
        List<Member> result = memberRepository.findByUsername(Arrays.asList("AAA","BBB"));


        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }
    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10);
//        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
//        memberRepository.save(m2);

        Member member = memberRepository.findMemberByUsername("AAA");
        System.out.println("member = " + member);
//        for (Member member : memberByUsername) {
//            System.out.println("member = " + member);
//        }
//        List<Member> findMember = memberRepository.findListByUsername("asdf");
//        Member findMember = memberRepository.findMemberByUsername("asdf");

//        Optional<Member> findMember = memberRepository.findOptionalByUsername("AAA");
//        System.out.println("findMember = " + findMember);
//        assertThatThrownBy(() -> memberRepository.findOptionalByUsername("AAA")).isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }
    @Test
    public void paging() {
        //given
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName()));

        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();
//        for (Member member : content) {
//            System.out.println("member = " + member);
//        }
//        System.out.println("totalElements = " + totalElements);
        assertThat(content.size()).isEqualTo(3);
      //  assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        //assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }
    @Test
    public void paging2() {
        //given
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3);
        //when
        Page<Member> page = memberRepository.findByAge(age,pageRequest);
        //then
        List<Member> content = page.getContent();
        for (Member member : content) {
            System.out.println("member = " + member);
        }
        assertThat(content.size()).isEqualTo(3);
          assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }
    @Test
    public void topTest() {
        //given
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        int age = 10;

        //when
        List<Member> findMembers = memberRepository.findTop3ByAge(age);

        //then
        for (Member findMember : findMembers) {
            System.out.println("findMember = " + findMember);
        }
        assertThat(findMembers.size()).isEqualTo(3);
    }
}