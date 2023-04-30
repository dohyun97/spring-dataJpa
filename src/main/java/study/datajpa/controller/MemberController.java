package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }
   //위에거랑 같은 결과
    @GetMapping("/membersV2/{id}")
    public String findMemberWithEnitity(@PathVariable("id") Member member){
        return member.getUsername();
    }
    //page json return entity
    @GetMapping("/members")  //5개 값 한페이지에. sort는 username dsec로
    public Page<Member> list(@PageableDefault(size = 5,sort = "username",direction = Sort.Direction.DESC) Pageable pageable){
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }

    //page json return memberDto
    @GetMapping("/membersV2")  //5개 값 한페이지에. sort는 username dsec로
    public Page<MemberDto> listDto(@PageableDefault(size = 5,sort = "username",direction = Sort.Direction.DESC) Pageable pageable){
        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDto> map = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));
        return map;
    }

    @PostConstruct
    public void init(){
        for(int i=0;i<100;i++){
            memberRepository.save(new Member("user"+i,i));
        }
    }
}
