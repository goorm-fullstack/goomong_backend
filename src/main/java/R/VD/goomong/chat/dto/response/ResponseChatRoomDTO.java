package R.VD.goomong.chat.dto.response;

import R.VD.goomong.chat.model.ChatMessage;
import R.VD.goomong.chat.model.ChatRoom;
import R.VD.goomong.item.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseChatRoomDTO {
    private String roomId;
    private String roomName;
    private String lastMessage;
    private ResponseChatItem itemDto;
    private LocalDateTime lastDate;

    public ResponseChatRoomDTO(ChatRoom chatRoom, Item item, String roomName) {
        this.roomId = chatRoom.getRoomId().toString();
        this.roomName = roomName;
        if (item != null)
            this.itemDto = new ResponseChatItem(item);

        ChatMessage lastChatMessage = findLastMessage(chatRoom.getMessages());
        if (lastChatMessage != null) {
            this.lastMessage = lastChatMessage.getMessage();
            this.lastDate = lastChatMessage.getRegDate();
        }
    }

    private ChatMessage findLastMessage(List<ChatMessage> messages) {
        if (messages == null || messages.isEmpty()) return null;

        return messages.stream()
                .max(Comparator.comparing(ChatMessage::getRegDate))
                .orElse(null);
    }
}