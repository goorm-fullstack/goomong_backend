package R.VD.goomong.chat.controller;

import R.VD.goomong.chat.dto.request.RequestChatRoomDTO;
import R.VD.goomong.chat.dto.request.RequestChatSoftDelete;
import R.VD.goomong.chat.dto.request.RequestItemChatRoomDTO;
import R.VD.goomong.chat.dto.response.ResponseChatRoomDTO;
import R.VD.goomong.chat.service.ChatRoomService;
import R.VD.goomong.global.model.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "chatRoom", description = "채팅방 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅방 조회", description = "멤버(memberId)를 이용하여 채팅방을 조회합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "채팅방 조회 성공", content = @Content(schema = @Schema(implementation = ResponseChatRoomDTO.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{memberId}")
    public ResponseEntity<List<ResponseChatRoomDTO>> getChatRoomList(@PathVariable Long memberId) {
        return new ResponseEntity<>(chatRoomService.findByMemberId(memberId), HttpStatus.OK);
    }

    @Operation(summary = "채팅방 생성", description = "멤버(memberId)와 아이템(itemId)를 이용하여 채팅방을 생성합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "채팅방 생성 성공", content = @Content(schema = @Schema(implementation = ResponseChatRoomDTO.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<ResponseChatRoomDTO> createItemChatRoom(@RequestBody RequestItemChatRoomDTO chatRoomDTO) {
        return new ResponseEntity<>(chatRoomService.createItemChatRoom(chatRoomDTO), HttpStatus.OK);
    }

    @Operation(summary = "채팅방 생성", description = "요청멤버(requestMemberId)와 요청받는멤버(responseMemberId)를 이용하여 채팅방을 생성합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "채팅방 생성 성공", content = @Content(schema = @Schema(implementation = ResponseChatRoomDTO.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/customer-center")
    public ResponseEntity<ResponseChatRoomDTO> createChatRoom(@RequestBody RequestChatRoomDTO chatRoomDTO) {
        return new ResponseEntity<>(chatRoomService.createChatRoom(chatRoomDTO), HttpStatus.OK);
    }

    @Operation(summary = "채팅방 삭제", description = "채팅방(roomId)와 멤버(memberId)를 이용하여 채팅방을 삭제합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "채팅방 삭제 성공", content = @Content(schema = @Schema(implementation = RequestChatSoftDelete.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping
    public ResponseEntity softDelete(@RequestBody RequestChatSoftDelete requestChatSoftDelete) {
        chatRoomService.softDelete(requestChatSoftDelete);
        return new ResponseEntity(HttpStatus.OK);
    }
}