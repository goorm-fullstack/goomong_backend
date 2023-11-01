package R.VD.goomong.chat.service;

import R.VD.goomong.chat.dto.request.RequestChatSoftDelete;
import R.VD.goomong.chat.dto.request.RequestChatUserDTO;
import R.VD.goomong.chat.dto.response.ResponseChatRoomDTO;
import R.VD.goomong.chat.exception.NotFoundChatRoom;
import R.VD.goomong.chat.model.ChatRoom;
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
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public List<ChatRoom> findAllRoom() {
        return chatRoomRepository.findAll();
    }

    public ResponseChatRoomDTO findByRoomUUID(String roomUUID) {
        return new ResponseChatRoomDTO(chatRoomRepository.findByRoomUUID(UUID.fromString(roomUUID))
                .orElseThrow(() -> new NotFoundChatRoom("존재하지 않는 채팅방 입니다.")));
    }

    public List<ResponseChatRoomDTO> findByMemberId(Long memberId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByMember_Id(memberId);
        return chatRooms.stream().map(ResponseChatRoomDTO::new).toList();
    }

    @Transactional
    public ResponseChatRoomDTO createChatRoom(RequestChatUserDTO requestChatUserDTO) {
        UUID uuid = UUID.randomUUID();
        Member requestUser = memberRepository.findById(requestChatUserDTO.getRequestUserId())
                .orElseThrow(() -> new NotFoundMember("존재하지 않는 사용자입니다."));
        Member responseUser = memberRepository.findById(requestChatUserDTO.getResponseUserId())
                .orElseThrow(() -> new NotFoundMember("존재하지 않는 사용자입니다."));

        ChatRoom requestChatRoom = ChatRoom.builder() // 구매자 또는 일반 유저에게 보이는 채팅방
                .roomUUID(uuid)
                .roomName(responseUser.getName())
                .member(requestUser)
                .build();
        ChatRoom responseChatRoom = ChatRoom.builder() // 판매자 또는 관리자에게 보이는 채팅방
                .roomUUID(uuid)
                .roomName(requestUser.getName())
                .member(responseUser)
                .build();

        chatRoomRepository.save(responseChatRoom);
        return new ResponseChatRoomDTO(chatRoomRepository.save(requestChatRoom));
    }

    public void softDelete(RequestChatSoftDelete requestChatSoftDelete) {

        UUID uuid = UUID.fromString(requestChatSoftDelete.getRoomUUID());

        Member member = memberRepository.findById(requestChatSoftDelete.getMemberId())
                .orElseThrow(() -> new NotFoundMember("존재하지 않는 사용자입니다."));
        ChatRoom chatRoom = chatRoomRepository.findByRoomUUIDAndMember(uuid, member)
                .orElseThrow(() -> new NotFoundChatRoom("존재하지 않는 채팅방입니다."));
        chatRoomRepository.delete(chatRoom);
    }
}
