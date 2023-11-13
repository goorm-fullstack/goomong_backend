package R.VD.goomong.member.controller;

import R.VD.goomong.member.dto.response.ResponseMemberDto;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

    //카카오톡 로그인
    @GetMapping("/kakao/callback")
    public String kakaoCallBack(@RequestParam String code) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "116cb3fda2149f8eaddf828c4f308179");
        params.add("redirect_uri", "http://localhost:8080/api/member/kakao/callback");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, kakaoTokenRequest, String.class);

        return "카카오 토큰 요청 완료 : 토큰 요청 응답 : " + response.getBody();
    }

}
