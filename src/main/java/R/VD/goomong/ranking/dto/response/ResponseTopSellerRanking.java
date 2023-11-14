package R.VD.goomong.ranking.dto.response;

import R.VD.goomong.ranking.model.RankingType;
import R.VD.goomong.ranking.model.TopSellerRanking;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTopSellerRanking {

    private Long memberId;

    private String memberName;

    private Long count;

    private RankingType type;

    public ResponseTopSellerRanking(TopSellerRanking ranking) {
        this.memberId = ranking.getMember().getId();
        this.memberName = ranking.getMember().getMemberName();
        this.count = ranking.getCount();
        this.type = ranking.getRankingType();
    }

}