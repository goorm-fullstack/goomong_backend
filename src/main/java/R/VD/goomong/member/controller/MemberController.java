package R.VD.goomong.member.controller;

import R.VD.goomong.member.dto.request.RequestMember;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    //CREATE
    //회원가입
    @PostMapping("/save")
    public ResponseEntity<Member> saveMember(@RequestBody RequestMember requestMember) {
        memberService.save(requestMember);

        return ResponseEntity.ok().build();
    }

    //READ
    //모든 회원 정보 불러오기
    @GetMapping("list")
    public List<Member> getAllMembers() {
        return memberService.getAll();
    }

    //회원 아이디로 회원 정보 찾기
    @GetMapping("/memberId/{memberId}")
    public ResponseEntity<Member> findByMemberId(@PathVariable String memberId){
        Member member = memberService.findByMemberId(memberId);

        return ResponseEntity.ok(member);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Member> findById(@PathVariable Long id){
        Member member = memberService.findById(id);

        return ResponseEntity.ok(member);
    }

    //UPDATE


    //DELETE
    //회원 memberId로 삭제
    @DeleteMapping("/memberId/{memberId}")
    public void deleteByMemberId(@PathVariable String memberId){
        memberService.deleteByMemberId(memberId);
    }

    //회원 index로 삭제
    @DeleteMapping("/id/{id}")
    public void deleteById(@PathVariable Long id){
        memberService.deleteById(id);
    }

    //회원 index로 softdelete
    @PutMapping("/id/{id}")
    public ResponseEntity<Member> softDeleteById(@PathVariable Long id){
        Member member = memberService.softDeleteById(id);

        return ResponseEntity.ok(member);
    }

    //회원 memberId로 softdelete
    @PutMapping("/memberId/{memberId}")
    public ResponseEntity<Member> softDeleteByMemberId(@PathVariable String memberId){
        Member member = memberService.softDeleteByMemberId(memberId);

        return ResponseEntity.ok(member);
    }

}
