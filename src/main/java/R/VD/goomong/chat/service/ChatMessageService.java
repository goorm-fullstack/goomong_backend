package R.VD.goomong.chat.service;

import R.VD.goomong.chat.dto.request.RequestChatMessageDTO;
import R.VD.goomong.chat.dto.response.ResponseChatMessageDTO;
import R.VD.goomong.chat.exception.NotFoundException;
import R.VD.goomong.chat.model.ChatMessage;
import R.VD.goomong.chat.model.ChatRoom;
import R.VD.goomong.chat.repository.ChatMessageRepository;
import R.VD.goomong.chat.repository.ChatRoomRepository;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;

    public List<ResponseChatMessageDTO> getMessages(Long roomId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoom_RoomId(roomId);
        return chatMessages.stream().map(ResponseChatMessageDTO::new).toList();
    }

    @Transactional
    public void saveMessage(RequestChatMessageDTO requestChatMessageDTO) {
        Long roomId = requestChatMessageDTO.getRoomId();
        Long memberId = requestChatMessageDTO.getMemberId();
        
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 채팅방입니다."));
        Member sender = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));

        ChatMessage chatMessage = ChatMessage.builder()
                .message(requestChatMessageDTO.getMessage())
                .member(sender)
                .chatRoom(chatRoom)
                .build();
        chatMessageRepository.save(chatMessage);
    }
}
