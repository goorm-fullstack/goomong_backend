package R.VD.goomong.memberTest;

import R.VD.goomong.member.dto.request.RequestMember;
import R.VD.goomong.member.dto.request.RequestUpdateDto;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.service.MemberService;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Transactional
public class memberTest {

    @Autowired
    private MemberService memberService;

    @Test
    void contextLoads() {
    }

    @Test
    void 회원가입및멤버아이디로회원찾기() {
        RequestMember requestMember = RequestMember.builder()
                .memberId("testId")
                .memberPassword("testPw")
                .memberName("test")
                .memberEmail("test@test.com")
                .build();

        memberService.save(requestMember);

        Member member = memberService.findByMemberId("testId");

        Assertions.assertThat("testId").isEqualTo(member.getMemberId());
        System.out.println("memberSignupTime : " + member.getMemberSignupTime());
    }

    @Test
    void 모든회원찾기() {
        RequestMember requestMember1 = RequestMember.builder()
                .memberId("testId1")
                .memberPassword("testPw1")
                .memberName("test1")
                .memberEmail("test1@test.com")
                .build();

        RequestMember requestMember2 = RequestMember.builder()
                .memberId("testId2")
                .memberPassword("testPw2")
                .memberName("test2")
                .memberEmail("test2@test.com")
                .build();

        RequestMember requestMember3 = RequestMember.builder()
                .memberId("testId3")
                .memberPassword("testPw3")
                .memberName("test3")
                .memberEmail("test3@test.com")
                .build();

        memberService.save(requestMember1);
        memberService.save(requestMember2);
        memberService.save(requestMember3);

        List<Member> members = memberService.getAll();

        for (Member member : members) {
            System.out.println("Member ID: " + member.getMemberId());
            System.out.println("Member Name: " + member.getMemberName());
            System.out.println("Member Email: " + member.getMemberEmail());
            System.out.println("------------------------------");
        }
    }

    @Test
    void 아이디로회원찾기() {
        RequestMember requestMember1 = RequestMember.builder()
                .memberId("testId1")
                .memberPassword("testPw1")
                .memberName("test1")
                .memberEmail("test1@test.com")
                .build();

        RequestMember requestMember2 = RequestMember.builder()
                .memberId("testId2")
                .memberPassword("testPw2")
                .memberName("test2")
                .memberEmail("test2@test.com")
                .build();

        memberService.save(requestMember1);
        memberService.save(requestMember2);

        Member member = memberService.findById(1L);

        System.out.println("member.getMemberId() = " + member.getMemberId());

        Assertions.assertThat(requestMember1.getMemberId()).isEqualTo(member.getMemberId());
    }

    @Test
    void 회원index로삭제() {
        RequestMember requestMember1 = RequestMember.builder()
                .memberId("testId1")
                .memberPassword("testPw1")
                .memberName("test1")
                .memberEmail("test1@test.com")
                .build();

        memberService.save(requestMember1);

        Member member = memberService.findById(1L);

        System.out.println("memberId = " + member.getId());

        memberService.deleteById(1L);

        Member member1 = memberService.findById(1L);
        if(member1 == null){
            System.out.println("null");
        }
        else {
            System.out.println("member1 = " + member1);
        }

    }

    @Test
    void 회원memberId로삭제() {
        RequestMember requestMember1 = RequestMember.builder()
                .memberId("testId1")
                .memberPassword("testPw1")
                .memberName("test1")
                .memberEmail("test1@test.com")
                .build();

        memberService.save(requestMember1);

        Member member = memberService.findByMemberId("testId1");
        System.out.println("memberid : " + member.getMemberId());

        memberService.deleteByMemberId("testId1");

        Member member1 = memberService.findByMemberId("testId1");
        if(member1 == null){
            System.out.println("null");
        }
        else {
            System.out.println("member1 = " + member1);
        }
    }

    @Test
    void softDelete테스트() {
        RequestMember requestMember1 = RequestMember.builder()
                .memberId("testId1")
                .memberPassword("testPw1")
                .memberName("test1")
                .memberEmail("test1@test.com")
                .build();

        memberService.save(requestMember1);

        Member member = memberService.findByMemberId("testId1");
//        System.out.println("memberid : " + member.getMemberId());
//        memberService.softDeleteByMemberId(member.getMemberId());
//        Member member1 = memberService.findByMemberId("testId1");
//        System.out.println("member1.getMemberDeleteTime = " + member1.getMemberDeleteTime());
        Member member1 = memberService.findById(member.getId());
        System.out.println("member = " + member1);
        memberService.softDeleteById(member.getId());
        Member member2 = memberService.findById(member.getId());

        System.out.println("member2.getMemberDeleteTime = " + member2.getMemberDeleteTime());
    }

    @Test
    @Transactional
    void 회원정보수정() {
        RequestMember requestMember1 = RequestMember.builder()
                .memberId("testId1")
                .memberPassword("testPw1")
                .memberName("test1")
                .memberEmail("test1@test.com")
                .build();

        memberService.save(requestMember1);

        Member member = memberService.findByMemberId("testId1");

        RequestUpdateDto requestUpdate = RequestUpdateDto.builder()
                .memberId(member.getMemberId())
                .memberPassword("updatedPassword")
                .memberName("updatedName")
                .memberEmail("updated@test.com")
                .build();


        memberService.updateMemberByMemberId(member.getMemberId(), requestUpdate);

        Member member1 = memberService.findByMemberId("testId1");

        System.out.println(member);
        System.out.println(member1);
    }

}
