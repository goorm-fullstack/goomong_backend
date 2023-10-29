package R.VD.goomong.chat.dto.response;

import R.VD.goomong.chat.model.ChatRoom;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseChatRoomDTO {
    private String roomId;
    private String roomName;
    private LocalDateTime regDate;

    public ResponseChatRoomDTO(ChatRoom chatRoom, String roomName) {
        this.roomId = chatRoom.getRoomId().toString();
        this.roomName = roomName;
        this.regDate = chatRoom.getRegDate();
    }

}