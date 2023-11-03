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
    private Long roomId;
    private String message;
    private String senderName;
    private LocalDateTime regDate;

    public ResponseChatMessageDTO(ChatMessage chatMessage) {
        this.roomId = chatMessage.getChatRoom().getRoomId();
        this.message = chatMessage.getMessage();
        this.senderName = chatMessage.getMember().getMemberName();
        this.regDate = chatMessage.getRegDate();
    }
}
