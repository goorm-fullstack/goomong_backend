package R.VD.goomong.chat.dto.response;

import R.VD.goomong.chat.model.ChatRoom;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseChatRoomDTO {
    private String roomId;
    private String roomName;
    private ResponseItemDto itemDto;
    private ZonedDateTime regDate;

    public ResponseChatRoomDTO(ChatRoom chatRoom, Item item, String roomName) {
        this.roomId = chatRoom.getRoomId().toString();
        this.roomName = roomName;
        if (item != null)
            this.itemDto = new ResponseItemDto(item);
        this.regDate = chatRoom.getRegDate();
    }

}