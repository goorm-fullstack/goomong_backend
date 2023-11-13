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

    private Long buyZipCode;

    private String buySimpleAddress;

    private String buyDetailAddress;

    private Long saleZipCode;

    private String saleSimpleAddress;

    private String saleDetailAddress;

    private String memberRole;

    private Long memberLoginFailed = 0L; // 기본값 0으로 설정

    private Boolean isKakao = false;

    private String saleInfo;


    @Builder(toBuilder = true)
    public RequestMember(String saleInfo, String memberId, String memberPassword, String memberName, String memberEmail, LocalDateTime memberSignupTime, Long buyZipCode, String buySimpleAddress, String buyDetailAddress, Long saleZipCode, String saleSimpleAddress, String saleDetailAddress, String memberRole, Long memberLoginFailed, Boolean isKakao) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberSignupTime = memberSignupTime;
        this.buyZipCode = buyZipCode;
        this.buySimpleAddress = buySimpleAddress;
        this.buyDetailAddress = buyDetailAddress;
        this.saleZipCode = saleZipCode;
        this.saleSimpleAddress = saleSimpleAddress;
        this.saleDetailAddress = saleDetailAddress;
        this.memberRole = memberRole;
        this.memberLoginFailed = memberLoginFailed;
        this.isKakao = isKakao;
        this.saleInfo = saleInfo;
    }



    public Member toEntity(){
        return Member.builder()
                .memberId(memberId)
                .memberPassword(memberPassword)
                .memberEmail(memberEmail)
                .memberName(memberName)
                .memberSignupTime(memberSignupTime)
                .buyZipCode(buyZipCode)
                .buySimpleAddress(buySimpleAddress)
                .buyDetailAddress(buyDetailAddress)
                .saleZipCode(saleZipCode)
                .saleSimpleAddress(saleSimpleAddress)
                .saleDetailAddress(saleDetailAddress)
                .memberRole(memberRole)
                .memberLoginFailed(memberLoginFailed)
                .isKakao(isKakao)
                .saleInfo(saleInfo)
                .build();
    }
}