package R.VD.goomong.chat.repository;

import R.VD.goomong.chat.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByRoomUUID(UUID roomUUID);

    List<ChatRoom> findByMember_Id(Long memberId);

}
