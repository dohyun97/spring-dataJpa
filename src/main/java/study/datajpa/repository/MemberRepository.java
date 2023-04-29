package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username,int age);

    //@Query로 쿼리 정의하기. 메소드 이름 아무거나 해도 상관없어. 장점:문법오류를 발견가능
    @Query("select m from Member m where m.username=:username and m.age=:age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();
    //Dto
    @Query("select new study.datajpa.dto.MemberDto(m.id,m.username,t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();
    //In
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    //유연한 반환 타입 지원
    //List<Member> findByUsername(String name); //collection, 결과없음: 빈컬렉션 반환
    //Member findByUsername(String name); //단건. 결과없음: null, 결과 2건 이상: 예외터져
    Optional<Member> findByUsername(String name); //단건. 결과없음: optional.empty or can throw 결과2개 이상:예외터져
}
