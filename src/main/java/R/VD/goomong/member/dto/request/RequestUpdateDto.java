package R.VD.goomong.member.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestUpdateDto{

    private String memberId;
    private String memberPassword;
    private String memberName;
    private String memberEmail;
    private Long zipCode;
    private String simpleAddress;
    private String detailAddress;

    @Builder
    public RequestUpdateDto(String memberId, String memberPassword, String memberName, String memberEmail, Long zipCode, String simpleAddress, String detailAddress) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.zipCode = zipCode;
        this.simpleAddress = simpleAddress;
        this.detailAddress = detailAddress;
    }
}
