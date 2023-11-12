package R.VD.goomong.chat.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestChatSoftDelete {
    private Long memberId;
    private String roomUUID;
}
