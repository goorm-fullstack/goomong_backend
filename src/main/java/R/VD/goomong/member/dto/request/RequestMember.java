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

    private Long memberLoginFailed = 0L; // 기본값 0으로 설정

    private Boolean isKakao = false;

    @Builder(toBuilder = true)
    public RequestMember(Boolean isKakao, Long memberLoginFailed, String memberRole, String memberId, String memberPassword, String memberName, String memberEmail, LocalDateTime memberSignupTime, Long zipCode, String simpleAddress, String detailAddress) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberSignupTime = memberSignupTime;
        this.zipCode = zipCode;
        this.simpleAddress = simpleAddress;
        this.detailAddress = detailAddress;
        this.memberRole = memberRole;
        this.memberLoginFailed = memberLoginFailed;
        this.isKakao = isKakao;
    }

    public Member toEntity(){
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
                .memberLoginFailed(memberLoginFailed)
                .isKakao(isKakao)
                .build();
    }
}