package R.VD.goomong.chat.service;

import R.VD.goomong.chat.dto.request.RequestChatImageDTO;
import R.VD.goomong.chat.dto.request.RequestChatMessageDTO;
import R.VD.goomong.chat.dto.response.ResponseChatMessageDTO;
import R.VD.goomong.chat.exception.ChatNotFoundException;
import R.VD.goomong.chat.model.ChatMessage;
import R.VD.goomong.chat.model.ChatRoom;
import R.VD.goomong.chat.repository.ChatMessageRepository;
import R.VD.goomong.chat.repository.ChatRoomRepository;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.service.ImageService;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    public List<ResponseChatMessageDTO> getMessages(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new ChatNotFoundException("채팅방 " + roomId + " 은 존재하지 않습니다."));
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoom(chatRoom);
        return chatMessages.stream().map(ResponseChatMessageDTO::new).toList();
    }

    @Transactional
    public ResponseChatMessageDTO saveMessage(RequestChatMessageDTO requestChatMessageDTO) {
        Long roomId = requestChatMessageDTO.getRoomId();
        Long memberId = requestChatMessageDTO.getMemberId();

        log.info("roomId = {}, memberId = {}", roomId, memberId);

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatNotFoundException("채팅방 " + roomId + " 은 존재하지 않습니다."));
        Member sender = memberRepository.findById(memberId)
                .orElseThrow(() -> new ChatNotFoundException("멤버 " + memberId + " 은 존재하지 않습니다."));

        ChatMessage chatMessage = ChatMessage.builder()
                .message(requestChatMessageDTO.getMessage())
                .member(sender)
                .chatRoom(chatRoom)
                .build();
        chatMessageRepository.save(chatMessage);
        return new ResponseChatMessageDTO(chatMessage);
    }

    @Transactional
    public ResponseChatMessageDTO saveImage(RequestChatImageDTO chatImageDTO) {
        Long roomId = chatImageDTO.getRoomId();
        Long memberId = chatImageDTO.getMemberId();

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatNotFoundException("채팅방 " + roomId + " 은 존재하지 않습니다."));
        Member sender = memberRepository.findById(memberId)
                .orElseThrow(() -> new ChatNotFoundException("멤버 " + memberId + " 은 존재하지 않습니다."));

        List<Image> images = new ArrayList<>();

        MultipartFile[] uploadImage = chatImageDTO.getImage();
        if (uploadImage != null && uploadImage.length > 0)
            images = imageService.saveImage(uploadImage);

        ChatMessage chatMessage = ChatMessage.builder()
                .member(sender)
                .chatRoom(chatRoom)
                .imageList(images)
                .build();
        chatMessageRepository.save(chatMessage);
        return new ResponseChatMessageDTO(chatMessage);
    }
}
