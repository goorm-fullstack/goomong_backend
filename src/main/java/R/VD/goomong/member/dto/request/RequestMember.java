package R.VD.goomong.member.dto.request;

import R.VD.goomong.member.model.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RequestMember {

    private String memberId;

    private String memberPassword;

    private String memberName;

    private String memberEmail;

    private LocalDateTime memberSignupTime = LocalDateTime.now();

    @Builder(toBuilder = true)
    public RequestMember(String memberId, String memberPassword, String memberName, String memberEmail) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
    }

    public Member toEntity(){
        return Member.builder()
                .memberId(memberId)
                .memberPassword(memberPassword)
                .memberEmail(memberEmail)
                .memberName(memberName)
                .memberSignupTime(memberSignupTime)
                .build();
    }

}
