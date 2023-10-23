package goomong.order.dto.request;

import goomong.global.model.Address;
import goomong.item.model.Item;
import goomong.order.model.Order;
import goomong.order.model.Status;
import lombok.Data;

import java.util.List;

@Data
public class RequestOrderDto {
    private List<Item> orderItem;
    private Long memberId;
    private int price;
    private Address address;

    public Order toEntity() {
        return Order.builder()
                .orderItem(orderItem)
                .address(address)
                .status(Status.WAITING)
                .build();
    }
}
