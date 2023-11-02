package R.VD.goomong.chat.repository;

import R.VD.goomong.chat.model.ChatMessage;
import R.VD.goomong.chat.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);

    void deleteByChatRoom(ChatRoom chatRoom);
}