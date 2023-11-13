package R.VD.goomong.ranking.dto.response;

import R.VD.goomong.ranking.model.Ranking;
import R.VD.goomong.ranking.model.RankingPeriod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRanking {

    private Long memberId;

    private String memberName;

    private String itemCategoryName;

    private Long salesCount;

    private RankingPeriod period;

    public ResponseRanking(Ranking ranking) {
        this.memberId = ranking.getMember().getId();
        this.memberName = ranking.getMember().getMemberName();
        this.itemCategoryName = ranking.getItemCategory().getTitle();
        this.salesCount = ranking.getSalesCount();
        this.period = ranking.getPeriod();
    }

}