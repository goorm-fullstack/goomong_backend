package R.VD.goomong.ranking.dto.response;

import R.VD.goomong.member.model.Seller;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ResponseTopRanking {
    private Long memberId;
    private String memberName;
    private String imagePath;
    private String saleSido;
    private Long transaction;
    private Long totalSales;
    private Long reviewCount;
    private Float totalRating;

    public ResponseTopRanking(Seller seller) {
        this.memberId = seller.getId();
        this.memberName = seller.getMemberId();
        this.imagePath = seller.getImagePath();
        this.saleSido = seller.getSaleSido();
        this.transaction = seller.getTransactionCnt();
        this.totalSales = seller.getIncome();
        this.reviewCount = seller.getReviewCnt();
        this.totalRating = seller.getRate();
    }
}