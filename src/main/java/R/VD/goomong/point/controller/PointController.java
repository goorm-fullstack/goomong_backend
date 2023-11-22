package R.VD.goomong.point.controller;

import R.VD.goomong.global.model.ErrorResponseDTO;
import R.VD.goomong.point.dto.response.ResponsePoint;
import R.VD.goomong.point.dto.response.ResponsePointHistory;
import R.VD.goomong.point.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Point", description = "포인트 관련 API")
@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @Operation(summary = "포인트 조회", description = "멤버 (memberId)를 이용해 총합, 만료예정 포인트 조회", responses = {
            @ApiResponse(responseCode = "200", description = "문의 조회 성공", content = @Content(schema = @Schema(implementation = ResponsePoint.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{memberId}")
    public ResponseEntity<ResponsePoint> getTotalPoints(@PathVariable Long memberId) {
        int totalPoints = pointService.getTotalPoints(memberId);
        int expiringPoints = pointService.getExpiringPoints(memberId);

        ResponsePoint responsePoint = new ResponsePoint(totalPoints, expiringPoints);

        return new ResponseEntity<>(responsePoint, HttpStatus.OK);
    }

    @Operation(summary = "포인트 내역 조회", description = "멤버 (memberId)를 이용해 포인트 내역 조회", responses = {
            @ApiResponse(responseCode = "200", description = "문의 조회 성공", content = @Content(schema = @Schema(implementation = ResponsePointHistory.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{memberId}/history")
    public ResponseEntity<List<ResponsePointHistory>> getPointHistory(@PathVariable Long memberId,
                                                                      @PageableDefault(sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable) {
        List<ResponsePointHistory> history = pointService.getPointHistory(memberId, pageable);

        return new ResponseEntity<>(history, HttpStatus.OK);
    }

}