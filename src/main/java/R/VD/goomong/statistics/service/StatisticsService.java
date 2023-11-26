package R.VD.goomong.statistics.service;

import R.VD.goomong.statistics.dto.response.ReviewStatisDTO;
import R.VD.goomong.statistics.repository.StatisticsRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final BigDecimal MAX_RATING = new BigDecimal("5.0");
    private final StatisticsRepository statisticsRepository;

    public ReviewStatisDTO getCustomerReviewStatistics() {
        Tuple reviewStats = statisticsRepository.getReviewStatistics();
        Tuple orderStats = statisticsRepository.getOrderStatistics();

        Double averageRating = reviewStats.size() != 0 && reviewStats.get(0, Double.class) != null ? reviewStats.get(0, Double.class) : null;
        Long reviewCount = reviewStats.size() != 0 && reviewStats.get(1, Long.class) != null ? ((Number) reviewStats.get(1, Long.class)).longValue() : null;
        Long orderCount = orderStats.size() != 0 && orderStats.get(0, Long.class) != null ? ((Number) orderStats.get(0, Long.class)).longValue() : null;
        Long orderSum = orderStats.size() != 0 && orderStats.get(1, Long.class) != null ? ((Number) orderStats.get(1, Long.class)).longValue() : null;

        Double customerSatisfaction = averageRating != null ? calculateCustomerSatisfaction(averageRating) : null;

        return ReviewStatisDTO.builder()
                .allReviewAvg(averageRating)
                .allReviewCnt(reviewCount)
                .customerSatisfaction(customerSatisfaction)
                .allOrderCnt(orderCount)
                .allOrderPriceSum(orderSum)
                .build();
    }

    private Double calculateCustomerSatisfaction(Double averageRating) {
        return new BigDecimal(averageRating)
                .divide(MAX_RATING, 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
