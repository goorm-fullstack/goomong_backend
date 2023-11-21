package R.VD.goomong.point.controller;

import R.VD.goomong.point.exception.PointIllegalArgumentException;
import R.VD.goomong.point.model.PointEvent;
import R.VD.goomong.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointEventListener {

    private final PointService pointService;

    @EventListener
    public void handlePointEvent(PointEvent event) {

        switch (event.getEventType()) {
            case PAYMENT_COMPLETED:
                pointService.earnPoint(event.getMember(),
                        event.getAmount(),
                        "주문 적립\n" + event.getItemName(),
                        event.getOrderNumber());
                break;
            case POINT_SPENT:
                pointService.spendPoint(event.getMember(),
                        event.getAmount(),
                        "적립금 결제\n" + event.getItemName(),
                        event.getOrderNumber());
                break;
            case REFUND:
                pointService.cancelPoint(event.getMember(),
                        event.getAmount(),
                        "환불에 따른 적립금 취소\n" + event.getItemName(),
                        event.getOrderNumber());
            case POINT_REDEMPTION_CANCELLATION:
                pointService.redemptionPoint(event.getMember(),
                        event.getAmount(),
                        "환불에 따른 포인트 사용 취소\n" + event.getItemName(),
                        event.getOrderNumber());
                break;
            default:
                throw new PointIllegalArgumentException("알 수 없는 이벤트 타입: " + event.getEventType());
        }
    }

}
