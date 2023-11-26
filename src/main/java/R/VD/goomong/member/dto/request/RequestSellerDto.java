package R.VD.goomong.member.dto.request;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.member.model.Seller;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@Schema(description = "판매자 정보 등록")
public class RequestSellerDto {

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

    @Schema(description = "썸네일", implementation = Image.class)
    private Image image; // 썸네일

    @Schema(description = "거래수", example = "1")
    private Long transactionCnt; // 거래수

    @Schema(description = "리뷰수", example = "1")
    private Long reviewCnt; // 리뷰수

    public Seller toEntity() {
        return Seller.builder()
                .memberId(memberId != null ? memberId : null)
                .rate(rate != null ? rate : null)
                .income(income != null ? income : null)
                .description(description != null ? description : null)
                .reviewCnt(reviewCnt != null ? reviewCnt : null)
                .imagePath(image != null ? image.getPath() : null)
                .transactionCnt(transactionCnt != null ? transactionCnt : null)
                .saleDetailAddress(saleDetailAddress != null ? saleDetailAddress : null)
                .saleSido(saleSido != null ? saleSido : null)
                .saleSimpleAddress(saleSimpleAddress != null ? saleSimpleAddress : null)
                .saleZipCode(saleZipCode != null ? saleZipCode : null)
                .build();
    }
}
