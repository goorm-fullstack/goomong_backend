package R.VD.goomong.chat.repository;

import R.VD.goomong.chat.model.ChatRoom;
import R.VD.goomong.chat.model.ChatRoomMember;
import R.VD.goomong.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    @Query("SELECT crm, m.memberName FROM ChatRoomMember crm JOIN crm.member m WHERE crm.member.id = :memberId")
    List<Object[]> findByMemberIdWithJoinFetch(@Param("memberId") Long memberId);

    void deleteByChatRoomAndMember(ChatRoom chatRoom, Member member);
}