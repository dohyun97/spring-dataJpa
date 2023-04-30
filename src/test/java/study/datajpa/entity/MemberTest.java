package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MemberTest {
    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    public void testEntity(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",20,teamA);
        Member member3 = new Member("member3",30,teamB);
        Member member4 = new Member("member4",40,teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        for (Member member : members) {
            System.out.println("member = "+member);
            System.out.println("->member.team = "+member.getTeam());
        }

    }

    //Auditing - createDate, updateDate...
    @Test
    void JpaEventBaseEntity() throws InterruptedException {
        Member member = new Member("member1");
        memberRepository.save(member); //@Prepersist

        Thread.sleep(100);
        member.changeInfo("member2",member.getAge());

        em.flush(); //@PreUpdate
        em.clear();
        //when
        Member fidnMember = memberRepository.findById(member.getId()).get();
        //then
        System.out.println("findMember.createdDate = "+fidnMember.getCreatedDate());
        System.out.println("findMember.updatedDate = "+fidnMember.getUpdatedDate());
    }
}