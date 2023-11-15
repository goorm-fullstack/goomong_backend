package R.VD.goomong.chat.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestChatRoomDTO {
    private Long requestMemberId;
    private Long responseMemberId;
}