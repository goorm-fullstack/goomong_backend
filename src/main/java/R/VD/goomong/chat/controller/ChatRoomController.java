package R.VD.goomong.chat.controller;

import R.VD.goomong.chat.dto.request.RequestChatRoomDTO;
import R.VD.goomong.chat.dto.request.RequestChatSoftDelete;
import R.VD.goomong.chat.dto.request.RequestItemChatRoomDTO;
import R.VD.goomong.chat.dto.response.ResponseChatRoomDTO;
import R.VD.goomong.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/{memberId}")
    public ResponseEntity<List<ResponseChatRoomDTO>> getChatRoomList(@PathVariable Long memberId) {
        return new ResponseEntity<>(chatRoomService.findByMemberId(memberId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseChatRoomDTO> createItemChatRoom(@RequestBody RequestItemChatRoomDTO chatRoomDTO) {
        return new ResponseEntity<>(chatRoomService.createItemChatRoom(chatRoomDTO), HttpStatus.OK);
    }

    @PostMapping("/customer-center")
    public ResponseEntity<ResponseChatRoomDTO> createChatRoom(@RequestBody RequestChatRoomDTO chatRoomDTO) {
        return new ResponseEntity<>(chatRoomService.createChatRoom(chatRoomDTO), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity softDelete(@RequestBody RequestChatSoftDelete requestChatSoftDelete) {
        chatRoomService.softDelete(requestChatSoftDelete);
        return new ResponseEntity(HttpStatus.OK);
    }
}