package R.VD.goomong.statistics.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewStatisDTO {

    private Double allReviewAvg;

    private Long allReviewCnt;

    private Double customerSatisfaction;

    private Long allOrderCnt;

    private Long allOrderPriceSum;

    public ReviewStatisDTO setCustomerSatisfaction(Double avgRating) {
        BigDecimal satisfaction = new BigDecimal(avgRating)
                .divide(new BigDecimal(5.0), 2, RoundingMode.HALF_UP) // 5.0으로 나누고, 소수점 두 자리에서 반올림합니다.
                .multiply(new BigDecimal(100)) // 100을 곱하여 퍼센트로 변환합니다.
                .setScale(1, RoundingMode.HALF_UP); // 소수점 첫째 자리에서 반올림하여 결과를 설정합니다.

        this.customerSatisfaction = satisfaction.doubleValue(); // 계산된 값으로 고객 만족도를 설정합니다.
        return this;
    }

}
