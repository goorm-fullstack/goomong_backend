package R.VD.goomong.ranking.model;

import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.item.model.ItemCategory;
import R.VD.goomong.member.model.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE Ranking SET del_date = CURRENT_TIMESTAMP WHERE ranking_id = ?")
public class Ranking extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long rankingId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private ItemCategory itemCategory;

    private Long salesCount;

    @Enumerated(EnumType.STRING)
    private RankingPeriod period;

    private LocalDateTime delDate;
}
