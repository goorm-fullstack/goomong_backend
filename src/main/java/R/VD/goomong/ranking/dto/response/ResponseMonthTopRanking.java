package R.VD.goomong.ranking.dto.response;

import R.VD.goomong.ranking.model.Ranking;
import R.VD.goomong.ranking.model.RankingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.NumberFormat;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMonthTopRanking {

    private Long memberId;

    private String memberName;

    private String imagePath;

    private String count;

    private String category;

    public ResponseMonthTopRanking(Ranking ranking) {

        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.KOREA);

        this.memberId = ranking.getMember().getId();
        this.memberName = ranking.getMember().getMemberName();
        if (ranking.getRankingType() == RankingType.ORDER) {
            this.category = "주문";
            this.count = ranking.getCount().toString() + "건";
        } else if (ranking.getRankingType() == RankingType.REVIEW) {
            this.category = "리뷰";
            this.count = ranking.getCount().toString() + "건";
        } else {
            this.category = "판매 금액";
            this.count = formatter.format(ranking.getCount());
        }
    }

}