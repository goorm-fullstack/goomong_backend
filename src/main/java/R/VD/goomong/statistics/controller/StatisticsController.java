package R.VD.goomong.statistics.controller;

import R.VD.goomong.statistics.dto.response.ReviewStatisDTO;
import R.VD.goomong.statistics.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "statistics", description = "통계 API")
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Operation(summary = "고객 리뷰 통계 조회", description = "고객 리뷰 게시판 상단에 있는 통계 조회", responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ReviewStatisDTO.class))),
    })
    @GetMapping("/customer-review")
    public ResponseEntity<ReviewStatisDTO> getCustomerReviewStatis() {
        ReviewStatisDTO customerReviewStatistics = statisticsService.getCustomerReviewStatistics();
        return new ResponseEntity<>(customerReviewStatistics, HttpStatus.OK);
    }

}