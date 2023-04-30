package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
//@Rollback(value = false) 이거를 하면 commit 해서 db에 데이터가 남아
class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void testMember(){
        Member member =new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        //find by id
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);
        //find list
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        //count
        Long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);
        //delete
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        Long countAfterDelete = memberJpaRepository.count();
        assertThat(countAfterDelete).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan(){
        Member member1 = new Member("member1",10);
        Member member2 = new Member("member1",20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("member1", 15);
        assertThat(result.get(0).getAge()).isEqualTo(member2.getAge());
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void paging(){
        memberJpaRepository.save( new Member("member1",10));
       memberJpaRepository.save(new Member("member2",10));
        memberJpaRepository.save(new Member("member3",10));
       memberJpaRepository.save(new Member("member4",10));
        memberJpaRepository.save(new Member("member5",10));
        memberJpaRepository.save( new Member("member6",10));
        memberJpaRepository.save(new Member("member7",10));
        memberJpaRepository.save( new Member("member8",10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        //페이지 계산 공식 적용...
        // totalPage = totalCount / size ...
        // 마지막 페이지 ... // 최초 페이지 ..
        //spring data jpa에서 이거를 다 계산해줘


        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        for (Member member : members) {
            System.out.println("members = "+member);
        }
        assertThat(members.size()).isEqualTo(3);
        long totalCount = memberJpaRepository.totalCount(age);
        assertThat(totalCount).isEqualTo(8);
    }
    //벌크연산
    @Test
    void bulkUpdate(){
        memberJpaRepository.save( new Member("member1",10));
        memberJpaRepository.save(new Member("member2",20));
        memberJpaRepository.save(new Member("member3",30));
        memberJpaRepository.save(new Member("member4",40));
        memberJpaRepository.save(new Member("member5",50));
        //when
        int resultCount = memberJpaRepository.bulkAgePlus(20);
        //then
        assertThat(resultCount).isEqualTo(4);
    }

}