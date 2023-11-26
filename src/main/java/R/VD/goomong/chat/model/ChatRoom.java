package R.VD.goomong.chat.model;

import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.item.model.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE ChatRoom SET del_date = CURRENT_TIMESTAMP WHERE room_id = ?")
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private List<ChatMessage> messages;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ChatRoomMember> members;

    private LocalDateTime delDate;

    public void setItem(Item item) {
        this.item = item;
    }

}