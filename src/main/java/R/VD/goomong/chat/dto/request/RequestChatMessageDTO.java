package R.VD.goomong.chat.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestChatMessageDTO {
    private Long roomId;
    private String message;
    private Long memberId;
}