package R.VD.goomong.member.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestFindPassword {
    private String memberId;
    private String newPassword;

    @Builder
    public RequestFindPassword(String memberId, String newPassword) {
        this.memberId = memberId;
        this.newPassword = newPassword;
    }
}
