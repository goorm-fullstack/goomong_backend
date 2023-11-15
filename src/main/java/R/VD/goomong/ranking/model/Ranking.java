package R.VD.goomong.ranking.model;

import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.member.model.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE Ranking SET del_date = CURRENT_TIMESTAMP WHERE ranking_id = ?")
public class Ranking extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rankingId;

    @ManyToOne
    private Member member;

    private Long count;

    @Enumerated(EnumType.STRING)
    private RankingType rankingType;

    private LocalDateTime delDate;

}
