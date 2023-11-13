package R.VD.goomong.member.dto.response;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ResponseMember {

    private String memberId;

    private String memberPassword;

    private String memberName;

    private String memberEmail;

    private ZonedDateTime memberSignupTime;

    public ResponseMember(String memberId, String memberPassword, String memberName, String memberEmail, ZonedDateTime memberSignupTime) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberSignupTime = memberSignupTime;
    }
}
