package R.VD.goomong.ranking.controller;

import R.VD.goomong.global.model.ErrorResponseDTO;
import R.VD.goomong.ranking.dto.response.ResponseTopSellerRanking;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Ranking", description = "랭킹 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranking")
public class RankingController {

    private final RankingService rankingService;

    @Operation(summary = "랭킹 조회", description = "랭킹 상단에 위치한 월간 랭킹 조회", responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseTopSellerRanking.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<ResponseTopSellerRanking>> getRanking() {
        List<ResponseTopSellerRanking> ranking = rankingService.getRanking();
        return new ResponseEntity<>(ranking, HttpStatus.OK);
    }

}