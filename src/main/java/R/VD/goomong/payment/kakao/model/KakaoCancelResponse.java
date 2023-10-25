package R.VD.goomong.payment.kakao.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 카카오페이 결제 취소 응답 모델
 * 각 변수에 대한 정보는 공식 문서 참조
 * <a href="https://developers.kakao.com/docs/latest/ko/kakaopay/cancellation#cancellation-request">...</a>
 */
@Data
public class KakaoCancelResponse {
    private String aid;
    private String tid;
    private String cid;
    private String status;
    private String partner_order_id;
    private String partner_user_id;
    private Amount amount;
    private ApprovedCancelAmount approved_cancel_amount;
    private CanceledAmount canceled_amount;
    private CancelAvailableAmount cancel_available_amount;
    private String item_name;
    private String item_code;
    private int quantity;
    private LocalDateTime created_at;
    private LocalDateTime approved_at;
    private LocalDateTime canceled_at;
    private String payload;

    @Data
    private static class ApprovedCancelAmount {
        private int total;
        private int tax_free;
        private int vat;
        private int point;
        private int discount;
        private int green_deposit;
    }

    @Data
    private static class CanceledAmount {
        private int total;
        private int tex_free;
        private int vat;
        private int point;
        private int discount;
        private int green_deposit;
    }

    @Data
    private static class CancelAvailableAmount {
        private int total;
        private int tex_free;
        private int vat;
        private int point;
        private int discount;
        private int green_deposit;
    }
}
