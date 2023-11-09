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
                .divide(new BigDecimal(5.0), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100))
                .setScale(1, RoundingMode.HALF_UP);

        this.customerSatisfaction = satisfaction.doubleValue();
        return this;
    }

}
