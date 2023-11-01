package R.VD.goomong.chat.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestChatUserDTO {
    private Long requestUserId;
    private Long responseUserId;
}
