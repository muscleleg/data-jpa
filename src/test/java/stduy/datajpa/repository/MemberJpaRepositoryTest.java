package stduy.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import stduy.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional

@SpringBootTest
@Rollback(value = false)
class
MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);
        
        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }
    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);
//
//        //수정
//        findMember1.setUserName("member!!!!");
        //리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long deletedCount = memberJpaRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }
    @Test
    public void testNamedQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }
    @Test
    public void pagingPractice() {
        //given
        for(int i = 1; i <= 100; i++) {
            memberJpaRepository.save(new Member("member"+i, 10));
        }

        int pageNum = 10; //51~60

        int pageSize = 10;
        long totalCount = memberJpaRepository.findAll().size();
        int totalPage = (int) Math.ceil(totalCount / pageSize);


        int blockPage = 5; //블록당 페이지 수
        int startPage = ((pageNum-1)/blockPage)*blockPage+1;

        int tempEndPage = (((pageNum-1)/blockPage)+1)*blockPage;
        int endPage = (tempEndPage <= totalPage) ? tempEndPage : totalPage;



        int start = (pageNum-1)*pageSize+1;
        int tempEnd = pageNum*pageSize;
        int end = (tempEnd) <= totalCount ? tempEnd : (int)totalCount;

        int offset = start;
        int limit = end;

//        assertThat(start).isEqualTo(51);
//        System.out.println("start = " + start);
//        assertThat(end).isEqualTo(60);
//        System.out.println("end = " + end);
        for (int i = start; i <= end; i++) {
            System.out.println(i);
        }
        for (int i = startPage; i <= endPage; i++) {
            String page = String.valueOf(i);
            if(i==pageNum) page ="["+page+"]";
            if(i==endPage) System.out.println(page);
            else System.out.print(page+", ");
        }

    }
    @Test
    public void paging() {
        //given
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));

        int age = 10;
        int offset= 1;
        int limit = 3;


        //when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);
        //페이지 계산 공식 적용
        //totalPage = totalCount / size..
        //마지막 페이지 ...
        //최초 페이지..

        //then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
    }
    
}