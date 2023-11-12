package R.VD.goomong.chat.service;

import R.VD.goomong.chat.dto.request.RequestChatMessageDTO;
import R.VD.goomong.chat.dto.response.ResponseChatMessageDTO;
import R.VD.goomong.chat.model.ChatMessage;
import R.VD.goomong.chat.repository.ChatMessageRepository;
import R.VD.goomong.chat.repository.ChatRoomRepository;
import R.VD.goomong.member.exception.NotFoundMember;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;

    public List<ResponseChatMessageDTO> getMessages(String roomUUID) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByRoomUUID(UUID.fromString(roomUUID));
        return chatMessages.stream().map(ResponseChatMessageDTO::new).toList();
    }

    @Transactional
    public void saveMessage(RequestChatMessageDTO requestChatMessageDTO) {
        UUID roomUUID = UUID.fromString(requestChatMessageDTO.getRoomUUID());
        Long memberId = requestChatMessageDTO.getMemberId();

        Member sender = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMember("존재하지 않는 사용자입니다."));

        ChatMessage chatMessage = ChatMessage.builder()
                .message(requestChatMessageDTO.getMessage())
                .roomUUID(roomUUID)
                .sender(sender)
                .build();
        chatMessageRepository.save(chatMessage);
    }
}
