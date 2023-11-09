package R.VD.goomong.statistics.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewStatisDTO {

    private Double allReviewAvg;

    private Long allReviewCnt;

    private Long allOrderCnt;

    private Long allOrderPriceSum;

}
