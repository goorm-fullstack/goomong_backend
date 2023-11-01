package R.VD.goomong.payment.kakao.controller;

import R.VD.goomong.order.dto.request.RequestOrderDto;
import R.VD.goomong.payment.kakao.dto.RequestKakaoPay;
import R.VD.goomong.payment.kakao.exception.ExceptionCode;
import R.VD.goomong.payment.kakao.exception.KakaoPayLogicException;
import R.VD.goomong.payment.kakao.model.KakaoCancelResponse;
import R.VD.goomong.payment.kakao.model.KakaoPayApproveResponse;
import R.VD.goomong.payment.kakao.model.KakaoPayResponse;
import R.VD.goomong.payment.kakao.service.KakaoPayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

/**
 * 카카오페이 결제 관련 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment/kakao")
public class KakaoPayController {
    private final KakaoPayService kakaoPayService;

    // 결제 시작
    @PostMapping("/ready")
    public ResponseEntity<KakaoPayResponse> ready(@Valid @RequestBody RequestKakaoPay requestKakaoPay, Model model) {
        return ResponseEntity.ok(kakaoPayService.kakaoPayReady(requestKakaoPay, model));
    }

    // 결제 완료
    @GetMapping("/success")
    public ResponseEntity<KakaoPayApproveResponse> afterPayRequest(
            @RequestParam("pg_token") String pgToken,
            @RequestParam("partner_order_id") String orderNumber,
            @RequestParam("partner_user_id") String userId,
            @ModelAttribute("order") RequestOrderDto orderDto,
            SessionStatus status
    ) {
        return ResponseEntity.ok(kakaoPayService.approveResponse(pgToken, orderNumber, userId, orderDto, status));
    }

    // 결제 취소
    @GetMapping("/cancel")
    public ResponseEntity<?> cancel() {
        throw new KakaoPayLogicException(ExceptionCode.PAY_CANCEL.name());
    }

    // 결제 실패
    @GetMapping("/fail")
    public void fail() {
        throw new KakaoPayLogicException(ExceptionCode.PAY_FAILED.name());
    }

    // 환불
    @PostMapping("/refund")
    public ResponseEntity<KakaoCancelResponse> refund(@RequestParam Long id) {
        return ResponseEntity.ok(kakaoPayService.cancel(id));
    }
}
