package R.VD.goomong.member.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class ResponseLogin {
    private Long id;
    private String memberId;
    private String memberPassword;
    private String memberRole;

    @Builder
    public ResponseLogin(Long id, String memberId, String memberPassword, String memberRole) {
        this.id = id;
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberRole = memberRole;
    }
}
