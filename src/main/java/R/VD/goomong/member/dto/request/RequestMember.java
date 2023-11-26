package R.VD.goomong.member.dto.request;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.member.model.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class RequestMember {

    private String memberId;

    private String memberPassword;

    private String memberName;

    private String memberEmail;

    private LocalDateTime memberSignupTime = LocalDateTime.now();

    private Long buyZipCode;

    private String buySido;

    private String buySimpleAddress;

    private String buyDetailAddress;

    private Long saleZipCode;

    private String saleSido;

    private String saleSimpleAddress;

    private String saleDetailAddress;

    private String memberRole;

    private Long memberLoginFailed = 0L; // 기본값 0으로 설정

    private Boolean isKakao = false;

    private String saleInfo;

    private List<Image> profileImage;

    @Builder(toBuilder = true)
    public RequestMember(String memberId, String memberPassword, String memberName, String memberEmail, LocalDateTime memberSignupTime, Long buyZipCode, String buySido, String buySimpleAddress, String buyDetailAddress, Long saleZipCode, String saleSido, String saleSimpleAddress, String saleDetailAddress, String memberRole, Long memberLoginFailed, Boolean isKakao, String saleInfo, List<Image> profileImage) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberSignupTime = memberSignupTime;
        this.buyZipCode = buyZipCode;
        this.buySido = buySido;
        this.buySimpleAddress = buySimpleAddress;
        this.buyDetailAddress = buyDetailAddress;
        this.saleZipCode = saleZipCode;
        this.saleSido = saleSido;
        this.saleSimpleAddress = saleSimpleAddress;
        this.saleDetailAddress = saleDetailAddress;
        this.memberRole = memberRole;
        this.memberLoginFailed = memberLoginFailed;
        this.isKakao = isKakao;
        this.saleInfo = saleInfo;
        this.profileImage = profileImage;
    }



    public Member toEntity(){
        return Member.builder()
                .memberId(memberId)
                .memberPassword(memberPassword)
                .memberEmail(memberEmail)
                .memberName(memberName)
                .memberSignupTime(memberSignupTime)
                .buyZipCode(buyZipCode)
                .buySido(buySido)
                .buySimpleAddress(buySimpleAddress)
                .buyDetailAddress(buyDetailAddress)
                .saleZipCode(saleZipCode)
                .saleSido(saleSido)
                .saleSimpleAddress(saleSimpleAddress)
                .saleDetailAddress(saleDetailAddress)
                .memberRole(memberRole)
                .memberLoginFailed(memberLoginFailed)
                .isKakao(isKakao)
                .saleInfo(saleInfo)
                .profileImages(profileImage)
                .build();
    }
}
