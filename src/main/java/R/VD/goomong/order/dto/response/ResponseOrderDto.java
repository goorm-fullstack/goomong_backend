package goomong.order.dto.response;

import goomong.global.model.Address;
import goomong.item.model.Item;
import goomong.member.model.Member;
import goomong.order.model.Order;
import goomong.order.model.Status;
import lombok.Data;

import java.util.List;

@Data
public class ResponseOrderDto {
    private Long id;
    private List<Item> orderItem;
    private Member member;
    private int price;
    private Address address;
    private Status status;//배송 상태

    public ResponseOrderDto(Order order) {
        this.id = order.getId();
        this.orderItem = order.getOrderItem();
        this.member = order.getMember();
        this.price = order.getPrice();
        this.address = order.getAddress();
        this.status = order.getStatus();
    }
}
