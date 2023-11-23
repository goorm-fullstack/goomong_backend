package R.VD.goomong.chat.service;

import R.VD.goomong.chat.dto.request.RequestChatRoomDTO;
import R.VD.goomong.chat.dto.request.RequestChatSoftDelete;
import R.VD.goomong.chat.dto.request.RequestItemChatRoomDTO;
import R.VD.goomong.chat.dto.response.ResponseChatRoomDTO;
import R.VD.goomong.chat.exception.ChatNotFoundException;
import R.VD.goomong.chat.model.ChatRoom;
import R.VD.goomong.chat.model.ChatRoomMember;
import R.VD.goomong.chat.repository.ChatMessageRepository;
import R.VD.goomong.chat.repository.ChatRoomMemberRepository;
import R.VD.goomong.chat.repository.ChatRoomRepository;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.repository.ItemRepository;
import R.VD.goomong.member.exception.NotFoundMember;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public List<ChatRoom> findAllRoom() {
        return chatRoomRepository.findAll();
    }

    public List<ResponseChatRoomDTO> findByMemberId(Long memberId) {
        List<Object[]> results = chatRoomMemberRepository.findByMemberIdWithJoinFetch(memberId);
        List<ResponseChatRoomDTO> responseList = new ArrayList<>();

        for (Object[] row : results) {
            ChatRoomMember chatRoomMember = (ChatRoomMember) row[0];
            String opponentName = Objects.toString(row[1]);

            ChatRoom chatRoom = chatRoomMember.getChatRoom();
            Item item = chatRoom.getItem(); // ChatRoom에서 Item 정보 가져오기

            ResponseChatRoomDTO responseChatRoomDTO = new ResponseChatRoomDTO(chatRoom, item, opponentName);

            responseList.add(responseChatRoomDTO);
        }

        return responseList;
    }

    @Transactional
    public ResponseChatRoomDTO createItemChatRoom(RequestItemChatRoomDTO chatRoomDTO) {
        Long memberId = chatRoomDTO.getMemberId();
        Member buyer = memberRepository.findById(memberId)
                .orElseThrow(() -> new ChatNotFoundException("멤버 " + memberId + "는 찾을 수 없습니다."));

        Long itemId = chatRoomDTO.getItemId();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ChatNotFoundException("상품 " + itemId + "는 찾을 수 없습니다."));
        Member seller = item.getMember();

        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByItem(item);

        for(ChatRoom chatRoom : chatRoomList) {
            for(ChatRoomMember member : chatRoom.getMembers()) {
                if(Objects.equals(member.getMember().getId(), buyer.getId())) {
                    return new ResponseChatRoomDTO(chatRoom, item, seller.getMemberName());
                }
            }
        }

        Optional<ChatRoom> existingChatRoom = chatRoomRepository.findByItemAndMembers_Member(item, buyer);
        if (existingChatRoom.isPresent())
            return new ResponseChatRoomDTO(existingChatRoom.get(), item, seller.getMemberName());

        ChatRoom chatRoom = ChatRoom.builder()
                .build();

        ChatRoom room = chatRoomRepository.save(chatRoom);
        room.setItem(item);

        ChatRoomMember roomMember = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .member(buyer)
                .build();
        ChatRoomMember build = roomMember.toBuilder()
                .member(seller).build();

        chatRoomMemberRepository.save(roomMember);
        chatRoomMemberRepository.save(build);

        return new ResponseChatRoomDTO(chatRoom, item, seller.getMemberName());
    }

    @Transactional
    public ResponseChatRoomDTO createChatRoom(RequestChatRoomDTO chatRoomDTO) {
        Long requestMemberId = chatRoomDTO.getRequestMemberId();
        Member requestMember = memberRepository.findById(requestMemberId)
                .orElseThrow(() -> new ChatNotFoundException("멤버 " + requestMemberId + "는 찾을 수 없습니다."));

        Long responseMemberId = chatRoomDTO.getResponseMemberId();
        Member responseMember = memberRepository.findById(responseMemberId)
                .orElseThrow(() -> new ChatNotFoundException("멤버 " + responseMemberId + "는 찾을 수 없습니다."));

        ChatRoom chatRoom = new ChatRoom();

        chatRoomRepository.save(chatRoom);

        ChatRoomMember roomMember = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .member(requestMember)
                .build();
        ChatRoomMember build = roomMember.toBuilder()
                .member(responseMember).build();
        chatRoomMemberRepository.save(roomMember);
        chatRoomMemberRepository.save(build);

        return new ResponseChatRoomDTO(chatRoom, null, responseMember.getMemberName());
    }

    public void softDelete(RequestChatSoftDelete requestChatSoftDelete) {
        Long roomId = requestChatSoftDelete.getRoomId();
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatNotFoundException("채팅 " + roomId + "는 찾을 수 없습니다."));

        Long memberId = requestChatSoftDelete.getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ChatNotFoundException("멤버 " + memberId + "는 찾을 수 없습니다."));

        chatRoomRepository.delete(chatRoom);
        chatMessageRepository.deleteByChatRoom(chatRoom);
        chatRoomMemberRepository.deleteByChatRoomAndMember(chatRoom, member);
    }
}
