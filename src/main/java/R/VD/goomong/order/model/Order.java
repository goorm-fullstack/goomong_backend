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

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany
    @JsonIgnore
    private List<Item> orderItem = new ArrayList<>();

    @ManyToOne
    private Member member;

    private int price;
    private String orderNumber;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private Status status;//배송 상태

    public void setMember(Member member) {
        this.member = member;
    }

    public void setOrderItem(List<Item> itemList) {
        this.orderItem = itemList;
    }

    // 재능 기부 작업 시작
    public void working() {
        this.status = Status.CONTINUE;
    }

    public void jobsFinish() {
        this.status = Status.COMPLETE;
    }

    // 가격 계산
    public void calculatePrice() {
        int result = 0;
        for (Item item : orderItem) {
            result += item.getPrice();
        }

        price = result;
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
