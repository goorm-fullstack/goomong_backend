package R.VD.goomong.order.model;

import R.VD.goomong.global.model.Address;
import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.order.exception.AlreadyCompleteOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @JsonIgnore
    private Item orderItem;

    @ManyToOne
    @JsonIgnore
    private Member member;

    private int price;

    private int point;

    private String orderNumber;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private Status status;//배송 상태

    public void setMember(Member member) {
        this.member = member;
    }

    public void setOrderItem(Item itemList) {
        this.orderItem = itemList;
    }

    // 재능 기부 작업 시작
    public void working() {
        this.status = Status.CONTINUE;
    }

    public void jobsFinish() {
        this.status = Status.COMPLETE;
    }

    // 환불 신청
    public void refund() {
        if (status == Status.COMPLETE) {
            throw new AlreadyCompleteOrder("이미 완료된 주문입니다. 완료된 주문을 환불하는 경우, 판매자와 협의하셔야 합니다");
        }

        this.status = Status.REFUND;
    }

    // 환불 완료
    public void refundComplete() {
        this.status = Status.REFUNDED;
    }
}
