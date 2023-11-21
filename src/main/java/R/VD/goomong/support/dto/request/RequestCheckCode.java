package R.VD.goomong.support.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestCheckCode {
    private String memberEmail;
    private String code;

    @Builder
    public RequestCheckCode(String memberEmail, String code) {
        this.memberEmail = memberEmail;
        this.code = code;
    }
}
