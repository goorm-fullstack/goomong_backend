package R.VD.goomong.ranking.controller;

import R.VD.goomong.ranking.dto.response.ResponseRanking;
import R.VD.goomong.ranking.model.RankingPeriod;
import R.VD.goomong.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranking")
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/{period}")
    public ResponseEntity<List<ResponseRanking>> getRanking(@RequestParam RankingPeriod period) {
        List<ResponseRanking> ranking = rankingService.getRanking(period);
        return new ResponseEntity<>(ranking, HttpStatus.OK);
    }

}