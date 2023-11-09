package R.VD.goomong.statistics.controller;

import R.VD.goomong.statistics.dto.response.ReviewStatisDTO;
import R.VD.goomong.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/customer-review")
    public ResponseEntity<ReviewStatisDTO> getCustomerReviewStatis() {
        ReviewStatisDTO customerReviewStatistics = statisticsService.getCustomerReviewStatistics();
        return new ResponseEntity<>(customerReviewStatistics, HttpStatus.OK);
    }

}
