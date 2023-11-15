package R.VD.goomong.payment.kakao.model;

import lombok.Data;

/**
 * 카카오페이 Amount 데이터형식
 */
@Data
public class Amount {
    private int total;
    private int tax_free;
    private int vat;
    private int point;
    private int discount;
    private int green_deposit;
}
