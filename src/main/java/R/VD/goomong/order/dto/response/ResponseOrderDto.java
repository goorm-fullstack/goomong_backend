package R.VD.goomong.order.dto.response;

import R.VD.goomong.global.model.Address;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.member.dto.response.ResponseMember;
import R.VD.goomong.order.model.Order;
import R.VD.goomong.order.model.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseOrderDto {
    private Long id;
    private ResponseItemDto orderItem;
    private ResponseMember member;
    private int price;
    private int point;
    private Address address;
    private Status status;//배송 상태
    private String orderNumber;
    private LocalDateTime regDate;

    public ResponseOrderDto(Order order) {
        this.id = order.getId();
        this.orderItem = new ResponseItemDto(order.getOrderItem());
        this.member = new ResponseMember(order.getMember());
        this.price = order.getPrice();
        this.point = order.getPoint();
        this.address = order.getAddress();
        this.status = order.getStatus();
        this.orderNumber = order.getOrderNumber();
        this.regDate = order.getRegDate();
    }
}
