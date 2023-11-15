package R.VD.goomong.chat.dto.response;

import R.VD.goomong.chat.model.ChatMessage;
import R.VD.goomong.image.model.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseChatMessageDTO {
    private Long roomId;
    private String message;
    private List<Image> imageList;
    private String senderName;
    private LocalDateTime regDate;

    public ResponseChatMessageDTO(ChatMessage chatMessage) {
        this.roomId = chatMessage.getChatRoom().getRoomId();
        this.message = chatMessage.getMessage();
        this.imageList = chatMessage.getImageList();
        this.senderName = chatMessage.getMember().getMemberName();
        this.regDate = chatMessage.getRegDate();
    }
}
