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

    private Long zipCode;

    private String simpleAddress;

    private String detailAddress;

    private String memberRole;


    @Builder(toBuilder = true)
    public RequestMember(String memberRole, String memberId, String memberPassword, String memberName, String memberEmail, LocalDateTime memberSignupTime, Long zipCode, String simpleAddress, String detailAddress) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberSignupTime = memberSignupTime;
        this.zipCode = zipCode;
        this.simpleAddress = simpleAddress;
        this.detailAddress = detailAddress;
        this.memberRole = memberRole;
    }

    public Member toEntity() {
        return Member.builder()
                .memberId(memberId)
                .memberPassword(memberPassword)
                .memberEmail(memberEmail)
                .memberName(memberName)
                .memberSignupTime(memberSignupTime)
                .zipCode(zipCode)
                .simpleAddress(simpleAddress)
                .detailAddress(detailAddress)
                .memberRole(memberRole)
                .build();
    }


}
