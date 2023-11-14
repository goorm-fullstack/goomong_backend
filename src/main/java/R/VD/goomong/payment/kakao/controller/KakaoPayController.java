package R.VD.goomong.payment.kakao.controller;

import R.VD.goomong.order.dto.request.RequestPayOrderDto;
import R.VD.goomong.payment.kakao.dto.RequestKakaoPay;
import R.VD.goomong.payment.kakao.exception.ExceptionCode;
import R.VD.goomong.payment.kakao.exception.KakaoPayLogicException;
import R.VD.goomong.payment.kakao.model.KakaoCancelResponse;
import R.VD.goomong.payment.kakao.model.KakaoPayApproveResponse;
import R.VD.goomong.payment.kakao.model.KakaoPayResponse;
import R.VD.goomong.payment.kakao.service.KakaoPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

/**
 * 카카오페이 결제 관련 컨트롤러
 */
@Tag(name = "KakaoPay", description = "카카오페이 API 서비스")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment/kakao")
public class KakaoPayController {
    private final KakaoPayService kakaoPayService;

    // 결제 시작
    @Operation(
            summary = "카카오페이 결제 준비",
            description = "카카오페이에 주문 정보를 토대로 결제 준비를 합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "결제 준비 완료",
                            content = @Content(schema = @Schema(implementation = KakaoPayResponse.class))
                    )
            }
    )
    @PostMapping("/ready")
    public ResponseEntity<KakaoPayResponse> ready(
            @Valid @RequestBody RequestKakaoPay requestKakaoPay,
            HttpServletRequest request,
            HttpServletResponse response) {
        return ResponseEntity.ok(kakaoPayService.kakaoPayReady(requestKakaoPay, request, response));
    }

    @Operation(
            summary = "카카오페이 결제 승인",
            description = "카카오페이에서 결제 요청을 승인합니다..",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "결제 완료",
                            content = @Content(schema = @Schema(implementation = KakaoPayApproveResponse.class))
                    )
            }
    )
    // 결제 완료
    @GetMapping("/success")
    public ResponseEntity<KakaoPayApproveResponse> afterPayRequest(
            @RequestParam("pg_token") String pgToken,
            @RequestParam("partner_order_id") String orderNumber,
            @RequestParam("partner_user_id") String userId,
            @SessionAttribute(name = "order", required = false) RequestPayOrderDto orderDto,
            SessionStatus status
    ) {
        return ResponseEntity.ok(kakaoPayService.approveResponse(pgToken, orderNumber, userId, orderDto, status));
    }

    @Operation(
            summary = "카카오페이 결제 취소",
            description = "카카오페이에서 결제를 취소합니다."
    )
    // 결제 취소
    @GetMapping("/cancel")
    public ResponseEntity<?> cancel() {
        throw new KakaoPayLogicException(ExceptionCode.PAY_CANCEL.name());
    }

    @Operation(
            summary = "카카오페이 결제 실패",
            description = "카카오페이에서 결제가 실패했습니다."
    )
    // 결제 실패
    @GetMapping("/fail")
    public void fail() {
        throw new KakaoPayLogicException(ExceptionCode.PAY_FAILED.name());
    }

    @Operation(
            summary = "카카오페이 환불 요청",
            description = "카카오페이에서 환불을 요청합니다."
    )
    // 환불
    @PostMapping("/refund")
    public ResponseEntity<KakaoCancelResponse> refund(@RequestParam Long id) {
        return ResponseEntity.ok(kakaoPayService.cancel(id));
    }
}
