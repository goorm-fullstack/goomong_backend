package R.VD.goomong.member.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class ResponseLogin {
    private Long id;
    private String memberId;
    private String memberPassword;

    @Builder
    public ResponseLogin(Long id, String memberId, String memberPassword) {
        this.id = id;
        this.memberId = memberId;
        this.memberPassword = memberPassword;
    }
}
