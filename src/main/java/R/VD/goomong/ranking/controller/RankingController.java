package R.VD.goomong.ranking.controller;

import R.VD.goomong.global.model.ErrorResponseDTO;
import R.VD.goomong.ranking.dto.response.ResponseRanking;
import R.VD.goomong.ranking.model.RankingPeriod;
import R.VD.goomong.ranking.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Ranking", description = "랭킹 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranking")
public class RankingController {

    private final RankingService rankingService;

    @Operation(summary = "랭킹 조회", description = "DAY, WEEK, MONTH 중 하나로 하시면 됩니다.", responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseRanking.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{period}")
    public ResponseEntity<List<ResponseRanking>> getRanking(@PathVariable String period) {
        RankingPeriod rankingPeriod = RankingPeriod.valueOf(period.toUpperCase());
        List<ResponseRanking> ranking = rankingService.getRanking(rankingPeriod);
        return new ResponseEntity<>(ranking, HttpStatus.OK);
    }

}