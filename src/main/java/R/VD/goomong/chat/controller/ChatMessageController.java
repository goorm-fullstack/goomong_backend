package R.VD.goomong.chat.controller;

import R.VD.goomong.chat.dto.request.RequestChatMessageDTO;
import R.VD.goomong.chat.dto.response.ResponseChatMessageDTO;
import R.VD.goomong.chat.service.ChatMessageService;
import R.VD.goomong.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate template;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload RequestChatMessageDTO requestChatMessageDTO) {
        log.info("CHAT {}", requestChatMessageDTO);

        chatMessageService.saveMessage(requestChatMessageDTO);

        template.convertAndSend("/sub/chat/room/" + requestChatMessageDTO.getRoomUUID(), requestChatMessageDTO);
    }

    @GetMapping("/api/chat/room/{roomUUID}")
    public ResponseEntity<List<ResponseChatMessageDTO>> getMessages(@PathVariable String roomUUID) {
        List<ResponseChatMessageDTO> responseChatMessageDTOList = chatMessageService.getMessages(roomUUID);
        return new ResponseEntity<>(responseChatMessageDTOList, HttpStatus.OK);
    }

}