package R.VD.goomong.member.controller;

import R.VD.goomong.member.dto.response.ResponseMemberDto;
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
    public void saveMember(@RequestBody Member member) {
        memberService.save(member);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ResponseMemberDto> findOneMember(@PathVariable Long memberId) {
        Member oneMember = memberService.findOneMember(memberId);
        return ResponseEntity.ok(oneMember.toResponseMemberDto());
    }
}
