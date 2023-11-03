package R.VD.goomong.member.controller;

import R.VD.goomong.member.dto.request.RequestLogin;
import R.VD.goomong.member.dto.request.RequestMember;
import R.VD.goomong.member.dto.request.RequestUpdateDto;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
// 회원 memberId로 회원 정보 수정
    @PutMapping("/update/memberId/{memberId}")
    public ResponseEntity<Member> updateByMemberId(@PathVariable String memberId, @RequestBody RequestUpdateDto requestUpdateDto) {
        Member updatedMember = memberService.updateMemberByMemberId(memberId, requestUpdateDto);
        return ResponseEntity.ok(updatedMember);
    }

    // 회원 index로 회원 정보 수정
    @PutMapping("/update/id/{id}")
    public ResponseEntity<Member> updateById(@PathVariable Long id, @RequestBody RequestUpdateDto requestUpdateDto) {
        Member updatedMember = memberService.updateMemberById(id, requestUpdateDto);
        return ResponseEntity.ok(updatedMember);
    }

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
    @PutMapping("/softDelete/id/{id}")
    public ResponseEntity<Member> softDeleteById(@PathVariable Long id){
        Member member = memberService.softDeleteById(id);

        return ResponseEntity.ok(member);
    }

    //회원 memberId로 softdelete
    @PutMapping("/softDelete/memberId/{memberId}")
    public ResponseEntity<Member> softDeleteByMemberId(@PathVariable String memberId){
        Member member = memberService.softDeleteByMemberId(memberId);

        return ResponseEntity.ok(member);
    }

    //로그인
    @PostMapping("/login")
    public String login(@RequestBody RequestLogin requestLogin, HttpServletResponse response){
        memberService.memberLogin(requestLogin);
        Cookie cookie = new Cookie("memberId", String.valueOf(requestLogin.getMemberId()));
        cookie.setMaxAge(60*30);
        response.addCookie(cookie);

        return "login";
    }

    //로그아웃
    @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        Cookie cookie = new Cookie("memberId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "logout";
    }

}
