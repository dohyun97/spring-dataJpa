package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long>,MemberRepositoryCustom {
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
    //Paging
    Page<Member> findByAge(int age, Pageable pageable); //count query 사용
    //slice
    //Slice<Member> findByAge(int age, Pageable pageable); //count query 사용x
    //count query 분리
    @Query(value = "select m from Member m left join m.team t" , countQuery = "select count(m.username) from Member m")
    Page<Member> findMemberAllCountBy(Pageable pageable);
    //벌크수정
    @Modifying(clearAutomatically = true) //벌크연산이후 영속성컨텍스트 초기화 = em.clear()용
    @Query("update Member m set m.age=m.age+1 where m.age>=:age")
    int bulkAgePlus(@Param("age") int age);

    //join fetch
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();
    //join fetch with @EntityGraph
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();
    //Jpql+entitygraph =>jpql에 join fetch 추가
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();
    //메서드 이름으로 쿼리에서 join fetch 추가
    @EntityGraph(attributePaths = {"team"})
    List<Member> findByAge(int age);

    //query hint
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly",value = "true"))
    Member findReadOnlyByUsername(String username);

}
