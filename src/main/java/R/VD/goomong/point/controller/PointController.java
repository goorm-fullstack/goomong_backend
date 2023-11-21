package R.VD.goomong.point.controller;

import R.VD.goomong.point.dto.request.RequestEarnPoint;
import R.VD.goomong.point.dto.request.RequestSpentPoint;
import R.VD.goomong.point.dto.response.ResponsePointHistory;
import R.VD.goomong.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @GetMapping("/{memberId}/total")
    public ResponseEntity<Integer> getTotalPoints(@PathVariable Long memberId) {
        int totalPoints = pointService.getTotalPoints(memberId);

        return ResponseEntity.ok(totalPoints);
    }

    @GetMapping("/{memberId}/history")
    public ResponseEntity<List<ResponsePointHistory>> getPointHistory(@PathVariable Long memberId) {
        List<ResponsePointHistory> history = pointService.getPointHistory(memberId);

        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    @PostMapping("/earn")
    public ResponseEntity<?> earnPoints(@RequestBody RequestEarnPoint requestEarnPoint) {
        pointService.earnPoints(requestEarnPoint);

        return ResponseEntity.ok("포인트 적립이 완료되었습니다..");
    }

    @PostMapping("/spend")
    public ResponseEntity<?> spendPoints(@RequestBody RequestSpentPoint requestSpentPoint) {
        pointService.spendPoints(requestSpentPoint);

        return ResponseEntity.ok("포인트 사용이 완료되었습니다.");
    }
}
