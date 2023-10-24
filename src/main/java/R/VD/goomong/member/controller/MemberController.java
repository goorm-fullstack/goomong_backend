package R.VD.goomong.member.controller;

import R.VD.goomong.member.dto.request.RequestMember;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/save")
    public ResponseEntity<Member> saveMember(@RequestBody RequestMember requestMember) {
        memberService.save(requestMember);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<Member> findByMemberId(@PathVariable String memberId){
        Member member = memberService.findByMemberId(memberId);

        return ResponseEntity.ok(member);
    }
}
