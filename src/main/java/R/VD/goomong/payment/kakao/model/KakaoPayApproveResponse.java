package R.VD.goomong.payment.kakao.model;

import lombok.Data;

/**
 * 카카오페이 결제 응답 모델
 * 자세한 내용은 공식 문서 참조
 */
@Data
public class KakaoPayApproveResponse {
    private String aid;
    private String tid;
    private String cid;
    private String sid;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;
    private Amount amount;
    private String item_name;
    private String item_code;
    private int quantity;
    private String created_at;
    private String approved_at;
    private String payload;
}
