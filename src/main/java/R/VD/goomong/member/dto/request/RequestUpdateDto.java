package R.VD.goomong.member.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestUpdateDto{

    private String memberId;
    private String memberPassword;
    private String memberName;
    private String memberEmail;
    private Long buyZipCode;
    private String buySimpleAddress;
    private String buyDetailAddress;
    private Long saleZipCode;
    private String saleSimpleAddress;
    private String saleDetailAddress;
    private String saleInfo;


    @Builder
    public RequestUpdateDto(String memberId, String memberPassword, String memberName, String memberEmail, Long buyZipCode, String buySimpleAddress, String buyDetailAddress, Long saleZipCode, String saleSimpleAddress, String saleDetailAddress, String saleInfo) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.buyZipCode = buyZipCode;
        this.buySimpleAddress = buySimpleAddress;
        this.buyDetailAddress = buyDetailAddress;
        this.saleZipCode = saleZipCode;
        this.saleSimpleAddress = saleSimpleAddress;
        this.saleDetailAddress = saleDetailAddress;
        this.saleInfo = saleInfo;
    }
}
