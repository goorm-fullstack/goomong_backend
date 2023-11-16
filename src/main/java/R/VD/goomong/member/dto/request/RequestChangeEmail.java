package R.VD.goomong.member.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestChangeEmail {
    private String memberId;
    private String memberEmail;

    @Builder
    public RequestChangeEmail(String memberId, String memberEmail) {
        this.memberId = memberId;
        this.memberEmail = memberEmail;
    }
}
