package R.VD.goomong.chat.repository;

import R.VD.goomong.chat.model.ChatRoom;
import R.VD.goomong.item.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByRoomIdAndItem(Long roomId, Item item);

}