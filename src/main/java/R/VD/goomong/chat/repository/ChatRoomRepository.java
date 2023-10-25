package R.VD.goomong.chat.repository;

import R.VD.goomong.chat.model.ChatRoom;
import R.VD.goomong.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByRoomUUID(UUID roomUUID);

    Optional<ChatRoom> findByRoomUUIDAndMember(UUID roomUUID, Member member);

    List<ChatRoom> findByMember_Id(Long memberId);
}