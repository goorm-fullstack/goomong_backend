package R.VD.goomong.member.dto.response;

import R.VD.goomong.global.model.PageInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@Schema(description = "판매자 response")
public class ResponseSellerDto {

    @Schema(description = "id", example = "1")
    private Long id;

    @Schema(description = "회원 아이디", example = "아이디")
    private String memberId; // 회원 아이디

    @Schema(description = "간단 소개", example = "소개")
    private String description; // 간단 소개

    @Schema(description = "총수익", example = "1")
    private Long income; // 총수익

    @Schema(description = "평점", example = "0.0")
    private Float rate; // 평점

    @Schema(description = "판매자 우편 번호", example = "000-000")
    private Long saleZipCode; //판매자 우편 번호

    @Schema(description = "판매자 시/도", example = "서울")
    private String saleSido; //판매자 시/도

    @Schema(description = "판매자 도로명 주소", example = "도로명 주소")
    private String saleSimpleAddress; //판매자 간단 주소

    @Schema(description = "상세 주소", example = "상세 주소")
    private String saleDetailAddress; //판매자 상세 주소

    @Schema(description = "썸네일 경로", example = "경로")
    private String imagePath; // 썸네일

    @Schema(description = "거래수", example = "1")
    private Long transactionCnt; // 거래수

    @Schema(description = "리뷰수", example = "1")
    private Long reviewCnt; // 리뷰수

    private LocalDateTime regDate; // 생성 날짜

    private PageInfo pageInfo; // 페이징 정보
}
