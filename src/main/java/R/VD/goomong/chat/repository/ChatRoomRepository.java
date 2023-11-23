package R.VD.goomong.chat.repository;

import R.VD.goomong.chat.model.ChatRoom;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByItemAndMembers_Member(Item item, Member member);

    List<ChatRoom> findAllByItem(Item item);
}