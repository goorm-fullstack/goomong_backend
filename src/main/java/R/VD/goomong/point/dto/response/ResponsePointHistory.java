package R.VD.goomong.point.dto.response;

import R.VD.goomong.point.model.Point;
import R.VD.goomong.point.model.PointType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponsePointHistory {

    private PointType type;

    private int amount;

    private String description;

    private String orderNumber;

    private LocalDateTime regDate;

    public ResponsePointHistory(Point point) {
        this.type = point.getType();
        this.amount = point.getAmount();
        this.description = point.getDescription();
        this.orderNumber = point.getOrderNumber();
        this.regDate = point.getRegDate();
    }

}
