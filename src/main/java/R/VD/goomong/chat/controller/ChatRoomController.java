package R.VD.goomong.chat.controller;

import R.VD.goomong.chat.dto.request.RequestChatSoftDelete;
import R.VD.goomong.chat.dto.request.RequestChatUserDTO;
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
    public ResponseEntity<ResponseChatRoomDTO> createChatRoom(@RequestBody RequestChatUserDTO requestChatUserDTO) {
        return new ResponseEntity<>(chatRoomService.createChatRoom(requestChatUserDTO), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity softDelete(@RequestBody RequestChatSoftDelete requestChatSoftDelete) {
        chatRoomService.softDelete(requestChatSoftDelete);
        return new ResponseEntity(HttpStatus.OK);
    }
}