package R.VD.goomong.order.dto.request;

import R.VD.goomong.global.model.Address;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.order.model.Order;
import R.VD.goomong.order.model.Status;
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
