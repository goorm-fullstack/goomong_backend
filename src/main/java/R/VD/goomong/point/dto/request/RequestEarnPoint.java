package R.VD.goomong.point.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestEarnPoint {

    private Long memberId;

    private int price;

    private String description;

    private String orderNumber;

}
