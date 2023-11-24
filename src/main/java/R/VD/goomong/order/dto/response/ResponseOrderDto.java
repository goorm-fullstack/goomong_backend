package R.VD.goomong.order.dto.response;

import R.VD.goomong.global.model.Address;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.order.model.Order;
import R.VD.goomong.order.model.Status;
import lombok.Data;

@Data
public class ResponseOrderDto {
    private Long id;
    private Item orderItem;
    private Member member;
    private int price;
    private int point;
    private Address address;
    private Status status;//배송 상태
    private String orderNumber;

    public ResponseOrderDto(Order order) {
        this.id = order.getId();
        this.orderItem = order.getOrderItem();
        this.member = order.getMember();
        this.price = order.getPrice();
        this.point = order.getPoint();
        this.address = order.getAddress();
        this.status = order.getStatus();
        this.orderNumber = order.getOrderNumber();
    }
}
