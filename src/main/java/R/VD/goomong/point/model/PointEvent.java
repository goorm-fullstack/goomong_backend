package R.VD.goomong.point.model;

import R.VD.goomong.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PointEvent {

    private Member member;

    private int amount;

    private String itemName;

    private String orderNumber;

    private EventType eventType;
    
}
