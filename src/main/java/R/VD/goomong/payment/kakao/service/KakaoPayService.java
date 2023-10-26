package R.VD.goomong.payment.kakao.service;

import R.VD.goomong.order.exception.NotExistOrder;
import R.VD.goomong.order.model.Order;
import R.VD.goomong.order.model.Status;
import R.VD.goomong.order.repository.OrderRepository;
import R.VD.goomong.payment.kakao.dto.RequestKakaoPay;
import R.VD.goomong.payment.kakao.model.KakaoCancelResponse;
import R.VD.goomong.payment.kakao.model.KakaoPayApproveResponse;
import R.VD.goomong.payment.kakao.model.KakaoPayResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class KakaoPayService {
    private final String cid = "TC0ONETIME";//가맹점 코드, 현재 값은 테스트용
    private final OrderRepository orderRepository;
    @Value("${adminKey}")
    private String adminKey;
    private KakaoPayResponse kakaoPayResponse;


    public KakaoPayResponse kakaoPayReady(RequestKakaoPay requestKakaoPay) {
        // 카카오페이 요청 양식
        MultiValueMap<String, String> parameters = setReadyParameter(requestKakaoPay);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        kakaoPayResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                KakaoPayResponse.class);

        return kakaoPayResponse;
    }

    public KakaoPayApproveResponse approveResponse(String pgToken) {
        // 카카오 요청
        MultiValueMap<String, String> parameters = setApproveParameter(pgToken);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                KakaoPayApproveResponse.class);
    }

    public KakaoCancelResponse cancel(Long id) {
        Optional<Order> findOrder = orderRepository.findById(id);
        if(findOrder.isEmpty()) {
            throw new NotExistOrder();
        }
        Order order = findOrder.get();
        if(findOrder.get().getStatus() != Status.REFUND) {
            throw new IllegalStateException("환불신청되지 않은 상품입니다.");
        }
        // 카카오페이 요청
        MultiValueMap<String, String> parameters = setCancelParameter(order);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();
        order.refundComplete();
        return restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/cancel",
                requestEntity,
                KakaoCancelResponse.class);
    }

    /**
     * 결제 요청 파라미터 세팅 함수
     * 각 파라미터에 대한 정보는 공식 문서를 참고해주세요.
     */
    private MultiValueMap<String, String> setReadyParameter(RequestKakaoPay requestKakaoPay) {
        Optional<Order> findOrder = orderRepository.findById(requestKakaoPay.getId());
        if(findOrder.isEmpty()) {
            throw new NotExistOrder();
        }
        Order order = findOrder.get();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("partner_order_id", "가맹점 주문 번호");
        parameters.add("partner_user_id", "가맹점 회원 ID");
        parameters.add("item_name", order.getOrderItem().get(0).getTitle());
        parameters.add("quantity", "1");//수량
        parameters.add("total_amount", String.valueOf(order.getPrice()));
        parameters.add("vat_amount", "0");//부가세
        parameters.add("tax_free_amount", "0");//비과세
        parameters.add("approval_url", requestKakaoPay.getSuccessURL()); // 성공 시 redirect url
        parameters.add("cancel_url", requestKakaoPay.getCancelURL()); // 취소 시 redirect url
        parameters.add("fail_url", requestKakaoPay.getFailURL()); // 실패 시 redirect url
        return parameters;
    }

    /**
     * 결제 승인 파라미터 세팅 함수
     */
    private MultiValueMap<String, String> setApproveParameter(String pgToken) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", kakaoPayResponse.getTid());
        parameters.add("partner_order_id", "가맹점 주문 번호");
        parameters.add("partner_user_id", "가맹점 회원 ID");
        parameters.add("pg_token", pgToken);
        return parameters;
    }

    /**
     * 결제 취소 파라미터 세팅
     */
    private MultiValueMap<String, String> setCancelParameter(Order order) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", kakaoPayResponse.getTid());
        parameters.add("cancel_amount", String.valueOf(order.getPrice()));
        parameters.add("cancel_tax_free_amount", "0");
        parameters.add("cancel_vat_amount", "0");
        return parameters;
    }

    /**
     * 카카오 요구 헤더값
     */
    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK " + adminKey;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }
}
