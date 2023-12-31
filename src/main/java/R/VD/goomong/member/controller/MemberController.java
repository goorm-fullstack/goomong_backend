package R.VD.goomong.member.controller;

import R.VD.goomong.global.model.PageInfo;
import R.VD.goomong.item.dto.response.ResponseItemPageDto;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.dto.request.*;
import R.VD.goomong.member.dto.response.ResponseLogin;
import R.VD.goomong.member.dto.response.ResponseMember;
import R.VD.goomong.member.model.KakaoOAuthToken;
import R.VD.goomong.member.model.KakaoProfile;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.service.MemberService;
import R.VD.goomong.review.dto.response.ResponseReviewDto;
import R.VD.goomong.review.model.Review;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Value("${cos.key}")
    private String cosKey;

    //CREATE
    //회원가입
    @PostMapping("/save")
    public ResponseEntity<Member> saveMember(@RequestBody RequestMember requestMember) {
        memberService.save(requestMember);

        return ResponseEntity.ok().build();
    }

    //READ
    //모든 회원 정보 불러오기
    @GetMapping("/list")
    public List<Member> getAllMembers() {
        return memberService.getAll();
    }

    //회원 아이디로 회원 정보 찾기
    @GetMapping("/memberId/{memberId}")
    public ResponseEntity<Member> findByMemberId(@PathVariable String memberId) {
        Member member = memberService.findByMemberId(memberId);

        return ResponseEntity.ok(member);
    }

    //회원 인덱스로 회원 정보 찾기
    @GetMapping("/id/{id}")
    public ResponseEntity<Member> findById(@PathVariable Long id) {
        Member member = memberService.findById(id);

        return ResponseEntity.ok(member);
    }

    // 추가 - @배진환
    @GetMapping("/{id}")
    public ResponseEntity<ResponseMember> findMember(@PathVariable Long id) {
        Member byId = memberService.findById(id);
        return ResponseEntity.ok(new ResponseMember(byId));
    }

    //회원 이메일로 회원 정보 찾기
    @GetMapping("/email/{email}")
    public ResponseEntity<Member> findByMemberEmail(@PathVariable String email) {
        Member member = memberService.findByMemberEmail(email);

        return ResponseEntity.ok(member);
    }

    //회원 이름으로 회원 정보 찾기
    @GetMapping("/memberName/{memberName}")
    public ResponseEntity<Member> findByMemberName(@PathVariable String memberName) {
        Member member = memberService.findByMemberName(memberName);

        return ResponseEntity.ok(member);
    }

    //회원 이름과 이메일로 회원 정보 찾기
    @PostMapping("/findId")
    public ResponseEntity<ResponseMember> findByMemberNameAndMemberEmail(RequestFindMember requestFindMember) {
        Member member = memberService.findByMemberNameAndMemberEmail(requestFindMember.getMemberName(), requestFindMember.getMemberName());

        return ResponseEntity.ok(new ResponseMember(member));
    }

    //UPDATE
    // 회원 memberId로 회원 정보 수정
    @PutMapping("/update/memberId")
    public ResponseEntity<Member> updateByMemberId(@RequestBody RequestUpdateDto requestUpdateDto) {
        Member updatedMember = memberService.updateMemberByMemberId(requestUpdateDto);
        return ResponseEntity.ok(updatedMember);
    }

    // 회원 index로 회원 정보 수정
    @PutMapping("/update/id/{id}")
    public ResponseEntity<Member> updateById(@PathVariable Long id, @RequestBody RequestUpdateDto requestUpdateDto) {
        Member updatedMember = memberService.updateMemberById(id, requestUpdateDto);
        return ResponseEntity.ok(updatedMember);
    }

    //memberId로 비밀번호 변경
    @PutMapping("/update/password")
    public ResponseEntity<Member> updatePasswordByMemberId(@RequestBody RequestChangePassword requestChangePassword) {
        Member member = memberService.changePasswordByMemberId(requestChangePassword);

        return ResponseEntity.ok(member);
    }

    //memberId로 비밀번호 찾기
    @PutMapping("/update/findpassword")
    public ResponseEntity<Member> findPasswordByMemberId(@RequestBody RequestFindPassword requestFindPassword) {
        Member member = memberService.findPasswordByMemberId(requestFindPassword);

        return ResponseEntity.ok(member);
    }


    @PutMapping("/update/email")
    public ResponseEntity<Member> updateEmailByMemberId(@RequestBody RequestChangeEmail requestChangeEmail) {
        Member member = memberService.changeEmailByMemberId(requestChangeEmail);

        return ResponseEntity.ok(member);
    }

    //memberId로 프로필 이미지 변경
    @PutMapping("/update/image")
    public ResponseEntity<Member> updateProfileImage(String memberId, MultipartFile[] multipartFiles) {
        Member member = memberService.changeProfileImageByMemberId(memberId, multipartFiles);

        return ResponseEntity.ok(member);
    }

    //DELETE
    //회원 memberId로 삭제
    @DeleteMapping("/memberId/{memberId}")
    public void deleteByMemberId(@PathVariable String memberId) {
        memberService.deleteByMemberId(memberId);
    }

    //회원 index로 삭제
    @DeleteMapping("/id/{id}")
    public void deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
    }

    //회원 index로 softdelete
    @PutMapping("/softDelete/id/{id}")
    public ResponseEntity<Member> softDeleteById(@PathVariable Long id) {
        Member member = memberService.softDeleteById(id);

        return ResponseEntity.ok(member);
    }

    //회원 memberId로 softdelete
    @PutMapping("/softDelete/memberId/{memberId}")
    public ResponseEntity<Member> softDeleteByMemberId(@PathVariable String memberId) {
        Member member = memberService.softDeleteByMemberId(memberId);

        return ResponseEntity.ok(member);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseLogin> login(@RequestBody RequestLogin requestLogin, HttpServletResponse response) {
        Member member = memberService.memberLogin(requestLogin);

        return ResponseEntity.ok().body(member.toResponseLoginDto());
    }

    //로그아웃
    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("memberId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "logout";
    }

    //카카오톡 로그인
    @GetMapping("/kakao/callback")
    public String kakaoCallBack(@RequestParam String code, HttpServletResponse response3) throws IOException {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "116cb3fda2149f8eaddf828c4f308179");
        params.add("redirect_uri", "https://keba1da28866ea.user-app.krampoline.com/api/member/kakao/callback");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, kakaoTokenRequest, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoOAuthToken kakaoOAuthToken = null;
        try {
            kakaoOAuthToken = objectMapper.readValue(response.getBody(), KakaoOAuthToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("kakaoOAuthToken.getAccess_token() = " + kakaoOAuthToken.getAccess_token());

        RestTemplate restTemplate2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + kakaoOAuthToken.getAccess_token());
        headers2.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = restTemplate2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class);

        System.out.println("response2.getBody() = " + response2.getBody());

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("카카오 회원번호: " + kakaoProfile.getId());
        System.out.println("카카오 이메일: " + kakaoProfile.getKakao_account().getEmail());
        System.out.println("카카오 유저네임: " + kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId());
        System.out.println("구몽서버 이메일: " + kakaoProfile.getKakao_account().getEmail());
        System.out.println("구몽서버 패스워드: " + cosKey);

        Member kakaoMember = Member.builder()
                .memberId(String.valueOf(kakaoProfile.getId()))
                .memberPassword(cosKey)
                .memberName(kakaoProfile.properties.nickname)
                .memberEmail(kakaoProfile.getKakao_account().getEmail())
                .isKakao(true)
                .build();

        Member byMemberId = memberService.findByMemberId(kakaoMember.getMemberId());

        if (byMemberId == null || byMemberId.getMemberId() == null) {
            System.out.println("기존 회원이 아님. 자동 회원가입 진행.");
            RequestMember requestMember = new RequestMember();
            requestMember.setMemberId(String.valueOf(kakaoProfile.getId()));
            requestMember.setMemberPassword(cosKey);
            requestMember.setMemberName(kakaoProfile.properties.nickname);
            requestMember.setMemberEmail(kakaoProfile.getKakao_account().getEmail());
            requestMember.setIsKakao(true);

            memberService.save(requestMember);
        }

        System.out.println("자동 로그인 진행.");
        RequestLogin requestLogin = new RequestLogin();
        requestLogin.setMemberId(String.valueOf(kakaoProfile.getId()));
        requestLogin.setMemberPassword(cosKey);
        memberService.memberLogin(requestLogin);

        Member member = memberService.findByMemberId(kakaoMember.getMemberId());

        response3.sendRedirect("https://keba1da28866ea.user-app.krampoline.com/?id=" + member.getId());

        return "kakao signup and login success";
    }

    /**
     * 회원아이디로 상품 리스트 페이징해서 가져오기 - @배진환
     *
     * @param memberId 회원 아이디
     * @param pageable 페이징
     * @return 조회된 상품 리스트
     */
    @Operation(summary = "회원아이디로 상품 리스트 페이징해서 가져오기")
    @Parameters(value = {
            @Parameter(name = "size", description = "페이지에 표시할 갯수", example = "10"),
            @Parameter(name = "page", description = "몇 번쨰 페이지인지", example = "0"),
            @Parameter(name = "pageable", hidden = true),
            @Parameter(name = "memberId", description = "회원 아이디", example = "회원아이디")
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseItemPageDto.class)))
    @GetMapping("/items")
    public ResponseEntity<ResponseItemPageDto> getItem(@RequestParam String memberId, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Item> memberItem = memberService.getMemberItem(memberId, pageable);
        int totalPages = memberItem.getTotalPages();

        return ResponseEntity.ok(new ResponseItemPageDto(memberItem.getContent(), totalPages));
    }

    /**
     * 회원아이디로 리뷰 리스트 페이징해서 가져오기 - @배진환
     *
     * @param memberId 회원 아이디
     * @param pageable 페이징
     * @return 조회된 리뷰 리스트
     */
    @Operation(summary = "회원아이디로 리뷰 리스트 페이징해서 가져오기")
    @Parameters(value = {
            @Parameter(name = "size", description = "페이지에 표시할 갯수", example = "10"),
            @Parameter(name = "page", description = "몇 번쨰 페이지인지", example = "0"),
            @Parameter(name = "pageable", hidden = true),
            @Parameter(name = "memberId", description = "회원 아이디", example = "회원아이디")
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseReviewDto.class))))
    @GetMapping("/reviews")
    public ResponseEntity<List<ResponseReviewDto>> getReview(@RequestParam String memberId, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Review> memberReview = memberService.getMemberReview(memberId, pageable);
        int totalPages = memberReview.getTotalPages();
        long totalElements = memberReview.getTotalElements();
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();

        List<ResponseReviewDto> list = new ArrayList<>();
        for (Review review : memberReview.getContent()) {
            ResponseReviewDto responseReviewDto = review.toResponseReviewDto();
            PageInfo pageInfo = PageInfo.builder()
                    .size(pageSize)
                    .totalPage(totalPages)
                    .totalElements(totalElements)
                    .page(pageNumber)
                    .build();
            ResponseReviewDto result = responseReviewDto.toBuilder()
                    .pageInfo(pageInfo)
                    .build();
            list.add(result);
        }
        return ResponseEntity.ok(list);
    }
}
