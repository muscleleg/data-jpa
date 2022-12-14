package stduy.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import stduy.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class MemberQueryRepository {
    private final EntityManager em;
    List<Member> findAllMembers() {
        return em.createQuery("select m from Member m").getResultList();
    }
}
