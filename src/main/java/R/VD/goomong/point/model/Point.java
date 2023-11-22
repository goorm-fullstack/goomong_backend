package R.VD.goomong.point.model;

import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.member.model.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Point extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private int amount;

    @Enumerated(EnumType.STRING)
    private PointType type;

    private String description; // 아이템 이름

    private String orderNumber; // 주문 번호

    // 적립
    public void earnPoints(int amount, String description, String orderNumber) {
        this.amount = amount;
        this.type = PointType.EARNED;
        this.description = description;
        this.orderNumber = orderNumber;
    }

    // 사용
    public void spendPoints(int amount, String description, String orderNumber) {
        this.amount = -amount; // 사용한 포인트는 음수로 표기
        this.type = PointType.SPENT;
        this.description = description;
        this.orderNumber = orderNumber;
    }
}