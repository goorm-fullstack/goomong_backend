package R.VD.goomong.chat.model;

import R.VD.goomong.member.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "room_uuid", columnDefinition = "BINARY(16)", nullable = false)
    private UUID roomUUID;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @Column(name = "reg_date")
    @CreationTimestamp
    private LocalDateTime regDate;

    @Column(name = "del_date")
    private LocalDateTime delDate;

    @ManyToOne
    private Member member;
}