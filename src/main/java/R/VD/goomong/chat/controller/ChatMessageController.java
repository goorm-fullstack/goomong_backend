package R.VD.goomong.chat.controller;

import R.VD.goomong.chat.dto.request.RequestChatImageDTO;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "chatMessage", description = "채팅 메세지 API")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate template;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload RequestChatMessageDTO requestChatMessageDTO) {

        log.info("sendMessage : {}", requestChatMessageDTO);
        ResponseChatMessageDTO responseChatMessageDTO = chatMessageService.saveMessage(requestChatMessageDTO);

        template.convertAndSend("/sub/chat/room/" + requestChatMessageDTO.getRoomId(), responseChatMessageDTO);
    }

    @Operation(summary = "이미지 전송", description = "방번호(roomId), 멤버(memberId)를 이용하여 이미지를 전송합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "메세지 전송 성공", content = @Content(schema = @Schema(implementation = ResponseChatMessageDTO.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/api/chat/sendImage")
    public ResponseEntity<Object> sendImage(@ModelAttribute RequestChatImageDTO chatImageDTO) {

        ResponseChatMessageDTO responseChatMessageDTO = chatMessageService.saveImage(chatImageDTO);

        template.convertAndSend("/sub/chat/room/" + responseChatMessageDTO.getRoomId(), responseChatMessageDTO);
        return ResponseEntity.ok().build();
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