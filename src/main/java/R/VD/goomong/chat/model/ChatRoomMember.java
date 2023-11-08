package R.VD.goomong.chat.model;

import R.VD.goomong.global.model.BaseDateEntity;
import R.VD.goomong.member.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.ZonedDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE ChatRoomMember SET del_date = CURRENT_TIMESTAMP WHERE room_id = ? AND member_id = ?")
public class ChatRoomMember extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private ZonedDateTime delDate;

}