package R.VD.goomong.chat.dto.response;

import R.VD.goomong.chat.model.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseChatMessageDTO {
    private String roomUUID;
    private String message;
    private String senderName;
    private LocalDateTime regDate;

    public ResponseChatMessageDTO(ChatMessage chatMessage) {
        this.roomUUID = chatMessage.getRoomUUID().toString();
        this.message = chatMessage.getMessage();
        this.senderName = chatMessage.getSender().getName();
        this.regDate = chatMessage.getRegDate();
    }
}
