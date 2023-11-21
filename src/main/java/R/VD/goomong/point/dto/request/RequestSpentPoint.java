package R.VD.goomong.point.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSpentPoint {

    private Long memberId;

    private int point;

    private String description;

    private String orderNumber;

}
