package R.VD.goomong.chat.dto.response;

import R.VD.goomong.chat.model.ChatRoom;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseChatRoomDTO {
    private String roomUUID;
    private String roomName;
    private LocalDateTime regDate;

    public ResponseChatRoomDTO(ChatRoom chatRoom) {
        this.roomUUID = chatRoom.getRoomUUID().toString();
        this.roomName = chatRoom.getRoomName();
        this.regDate = chatRoom.getRegDate();
    }
}