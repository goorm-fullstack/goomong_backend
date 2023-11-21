package R.VD.goomong.payment.kakao.dto;

import R.VD.goomong.order.dto.request.RequestPayOrderDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * 사용자에게 전달받을 파라미터값
 */
@Data
public class RequestKakaoPay {
    @Positive
    private int price;

    private int point;

    @NotNull
    @NotEmpty
    private String orderName;

    @NotNull
    @NotEmpty
    private String successURL;//성공시 리다이렉션할 주소

    @NotNull
    @NotEmpty
    private String failURL;//실패시 리다이렉션할 주소

    @NotNull
    @NotEmpty
    private String cancelURL;//취소시 리다이렉션할 주소

    @NotNull
    private RequestPayOrderDto orderDto;
}
