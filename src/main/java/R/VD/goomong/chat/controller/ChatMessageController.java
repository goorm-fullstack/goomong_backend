package R.VD.goomong.chat.controller;

import R.VD.goomong.chat.dto.request.RequestChatMessageDTO;
import R.VD.goomong.chat.dto.response.ResponseChatMessageDTO;
import R.VD.goomong.chat.service.ChatMessageService;
import R.VD.goomong.global.model.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "chatMessage", description = "채팅 메세지 API")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate template;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/api/chat/sendMessage")
    public void sendMessage(@Payload RequestChatMessageDTO requestChatMessageDTO) {

        ResponseChatMessageDTO responseChatMessageDTO = chatMessageService.saveMessage(requestChatMessageDTO);

        template.convertAndSend("/sub/chat/room/" + requestChatMessageDTO.getRoomId(), responseChatMessageDTO);
    }

    @Operation(summary = "메세지 조회", description = "방번호(roomId)를 이용하여 메세지를 조회합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "메세지 조회 성공", content = @Content(schema = @Schema(implementation = ResponseChatMessageDTO.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/api/chat/room/{roomId}")
    public ResponseEntity<List<ResponseChatMessageDTO>> getMessages(@PathVariable Long roomId) {
        List<ResponseChatMessageDTO> responseChatMessageDTOList = chatMessageService.getMessages(roomId);
        return new ResponseEntity<>(responseChatMessageDTOList, HttpStatus.OK);
    }

}