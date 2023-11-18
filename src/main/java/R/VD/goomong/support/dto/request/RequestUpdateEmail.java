package R.VD.goomong.support.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestUpdateEmail {
    String oldEmail;
    String newEmail;
}
