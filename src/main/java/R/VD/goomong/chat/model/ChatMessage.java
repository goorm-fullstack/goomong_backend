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
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    private Long messageId;

    @Column(name = "room_uuid", columnDefinition = "BINARY(16)", nullable = false)
    private UUID roomUUID;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "reg_date")
    @CreationTimestamp
    private LocalDateTime regDate;
    
    @ManyToOne
    private Member sender;
}