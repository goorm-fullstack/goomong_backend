package R.VD.goomong.statistics.service;

import R.VD.goomong.statistics.dto.response.ReviewStatisDTO;
import R.VD.goomong.statistics.repository.StatisticsRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public ReviewStatisDTO getCustomerReviewStatistics() {
        Tuple reviewStats = statisticsRepository.getReviewStatistics();
        Tuple orderStats = statisticsRepository.getOrderStatistics();

        Double averageRating = reviewStats.get(0, Double.class);

        return ReviewStatisDTO.builder()
                .allReviewAvg(averageRating)
                .allReviewCnt(reviewStats.get(1, Long.class))
                .customerSatisfaction(averageRating)
                .allOrderCnt(orderStats.get(0, Long.class))
                .allOrderPriceSum(orderStats.get(1, Long.class))
                .build();
    }
}
