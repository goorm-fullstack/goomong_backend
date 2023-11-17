package R.VD.goomong.support.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestCheckCode {
    private String email;
    private String code;

    @Builder
    public RequestCheckCode(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
