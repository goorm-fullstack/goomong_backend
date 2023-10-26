package R.VD.goomong.member.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RequestUpdateDto {

    private String memberId;
    private String memberName;
    private String memberPassword;
    private String memberEmail;

    @Builder
    public RequestUpdateDto(String memberId, String memberName, String memberPassword, String memberEmail) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberPassword = memberPassword;
        this.memberEmail = memberEmail;
    }

}
