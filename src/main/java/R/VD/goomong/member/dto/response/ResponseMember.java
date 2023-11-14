package R.VD.goomong.member.dto.response;

import R.VD.goomong.member.model.Member;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseMember {

    private String memberId;

    private String memberPassword;

    private String memberName;

    private String memberEmail;

    private LocalDateTime memberSignupTime;

    public ResponseMember(String memberId, String memberPassword, String memberName, String memberEmail, LocalDateTime memberSignupTime) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberSignupTime = memberSignupTime;
    }

    /**
     * 파라미터가 여러 개로 이루어진 생성자는 가독성이 좋지 않다고 생각해서
     * 임의로 생성자를 하나더 만들었습니다.
     *
     * @ 김경규 - 2023.11.14
     */
    public ResponseMember(Member member) {
        this.memberId = member.getMemberId();
        this.memberPassword = member.getMemberPassword();
        this.memberName = member.getMemberName();
        this.memberEmail = member.getMemberEmail();
        this.memberSignupTime = member.getMemberSignupTime();
    }
}
