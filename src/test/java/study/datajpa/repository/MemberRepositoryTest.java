package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@Transactional
@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    void testMember(){
        Member member =new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);
        //find by id
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);
        //find list
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        //count
        Long count = memberRepository.count();
        assertThat(count).isEqualTo(2);
        //delete
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        Long countAfterDelete = memberRepository.count();
        assertThat(countAfterDelete).isEqualTo(0);
    }
    @Test
    public void findByUsernameAndAgeGreaterThan(){
        Member member1 = new Member("member1",10);
        Member member2 = new Member("member1",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("member1", 15);
        assertThat(result.get(0).getAge()).isEqualTo(member2.getAge());
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testQuery(){
        Member member1 = new Member("member1",10);
        Member member2 = new Member("member1",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findUser("member1",10);
        assertThat(result.get(0).getAge()).isEqualTo(member1.getAge());
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void findUsernameList(){
        Member member1 = new Member("member1",10);
        Member member2 = new Member("member2",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> result = memberRepository.findUsernameList();
        assertThat(result).contains("member1","member2");
    }

    @Test
    void findMemberDto(){

        Team teamB = new Team("teamB");
        teamRepository.save(teamB);

        Member member1 = new Member("member1",10,teamB);

        memberRepository.save(member1);

        List<MemberDto> findMember = memberRepository.findMemberDto();
        assertThat(findMember.get(0).getTeamName()).isEqualTo("teamB");
        assertThat(findMember.get(0).getId()).isEqualTo(member1.getId());
        assertThat(findMember.get(0).getUsername()).isEqualTo(member1.getUsername());

    }

    @Test
    void findByNames(){
        Member member1 = new Member("member1",10);
        Member member2 = new Member("member2",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("member1","member2"));
        for (Member member : result) {
            System.out.println("member = "+result);
        }
    }

}