package R.VD.goomong.payment.kakao.model;

import lombok.Data;

/**
 * 카카오페이 응답 모델
 * 참고 자료 - @<a href="https://developers.kakao.com/docs/latest/ko/kakaopay/single-payment">...</a>
 */
@Data
public class KakaoPayResponse {
    private String tid;//결제 고유 변호, 20자
    private String pc;//요청 클라이언트가 모바일인 경우
    private String next_redirect_mobile_url;//요청한 클라이언트가 모바일 웹일 경우
    private String next_redirect_pc_url;//요청한 클라이언트가 PC 웹일 경우
    private String android_app_scheme;//카카오페이 결제 화면으로 이동하는 Android 앱 스킴(Scheme)
    private String ios_app_scheme;//카카오페이 결제 화면으로 이동하는 iOS 앱 스킴
    private String created_at;//결제 준비 요청 시간
}
