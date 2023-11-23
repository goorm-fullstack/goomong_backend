package R.VD.goomong.order.dto.request;

import R.VD.goomong.global.model.Address;
import R.VD.goomong.order.model.Order;
import R.VD.goomong.order.model.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class RequestPayOrderDto {
    @NotEmpty
    @NotNull
    private List<Long> orderItem;

    @NotEmpty
    @Positive
    private Long memberId;

    @NotEmpty
    @Positive
    private int price;

    @NotEmpty
    private int point;

    private String orderNumber;

    @NotEmpty
    @NotNull
    private Address address;

    public Order toEntity() {
        return Order.builder()
                .orderNumber(orderNumber)
                .address(address)
                .status(Status.WAITING)
                .orderNumber(orderNumber)
                .price(price)
                .build();
    }
}
