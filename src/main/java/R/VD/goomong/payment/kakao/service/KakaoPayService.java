package R.VD.goomong.payment.kakao.service;

import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.order.dto.request.RequestOrderDto;
import R.VD.goomong.order.exception.NotExistOrder;
import R.VD.goomong.order.model.Order;
import R.VD.goomong.order.model.Status;
import R.VD.goomong.order.repository.OrderRepository;
import R.VD.goomong.order.service.OrderService;
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
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class KakaoPayService {
    private final String cid = "TC0ONETIME";//가맹점 코드, 현재 값은 테스트용
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final MemberRepository memberRepository;
//    @Value("${adminKey}")
    private String adminKey;
    private KakaoPayResponse kakaoPayResponse;


    public KakaoPayResponse kakaoPayReady(RequestKakaoPay requestKakaoPay, Model model) {
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

        model.addAttribute("order", requestKakaoPay.getOrderDto());
        return kakaoPayResponse;
    }

    public KakaoPayApproveResponse approveResponse(String pgToken, String orderNumber, String userId, RequestOrderDto orderDto, SessionStatus status) {
        // 카카오 요청
        MultiValueMap<String, String> parameters = setApproveParameter(pgToken, orderNumber, userId);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoPayApproveResponse kakaoPayApproveResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                KakaoPayApproveResponse.class);

        orderService.createNewOrder(orderDto);//주문 정보 저장
        status.setComplete();//세션 클리어
        return kakaoPayApproveResponse;
    }

    public KakaoCancelResponse cancel(Long id) {
        Optional<Order> findOrder = orderRepository.findById(id);
        if (findOrder.isEmpty()) {
            throw new NotExistOrder();
        }
        Order order = findOrder.get();
        if (findOrder.get().getStatus() != Status.REFUND) {
            throw new IllegalStateException("환불신청되지 않은 상품입니다.");
        }
        // 카카오페이 요청
        MultiValueMap<String, String> parameters = setCancelParameter();

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
        Member member = memberRepository.findById(requestKakaoPay.getOrderDto().getMemberId()).orElseThrow();
        String orderNumber = generateRandomNumberWithDate();
        requestKakaoPay.getOrderDto().setOrderNumber(orderNumber);
        String memberHashCode = "123456";
        System.out.println("hashCode : " + memberHashCode);
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        requestKakaoPay.getOrderDto().setOrderNumber(generateRandomNumberWithDate());
        parameters.add("cid", cid);
        parameters.add("partner_order_id", orderNumber);
        parameters.add("partner_user_id", memberHashCode);
        parameters.add("item_name", requestKakaoPay.getOrderName());
        parameters.add("quantity", "1");//수량
        parameters.add("total_amount", String.valueOf(requestKakaoPay.getPrice()));
        parameters.add("vat_amount", "0");//부가세
        parameters.add("tax_free_amount", "0");//비과세
        parameters.add("approval_url", requestKakaoPay.getSuccessURL() + "?partner_order_id=" + orderNumber + "&partner_user_id=" + memberHashCode); // 성공 시 redirect url
        parameters.add("cancel_url", requestKakaoPay.getCancelURL()); // 취소 시 redirect url
        parameters.add("fail_url", requestKakaoPay.getFailURL()); // 실패 시 redirect url
        return parameters;
    }

    /**
     * 결제 승인 파라미터 세팅 함수
     */
    private MultiValueMap<String, String> setApproveParameter(String pgToken, String orderNumber, String userId) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", kakaoPayResponse.getTid());
        parameters.add("partner_order_id", orderNumber);
        parameters.add("partner_user_id", userId);
        parameters.add("pg_token", pgToken);
        return parameters;
    }

    /**
     * 결제 취소 파라미터 세팅
     */
    private MultiValueMap<String, String> setCancelParameter() {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", kakaoPayResponse.getTid());
        parameters.add("cancel_amount", "1");
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

    // 주문 번호 생성 로직입니다.
    private String generateRandomNumberWithDate() {
        // 현재 날짜 정보를 포함한 문자열 생성 (yyyyMMdd 형식)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String currentDate = dateFormat.format(new Date());
        // 6자리의 임의의 숫자열 생성
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000; // 100000부터 999999까지의 난수 생성
        // 현재 날짜 정보와 6자리의 임의의 숫자열을 조합하여 16자리의 숫자열 생성
        return currentDate + randomNumber;
    }
}
