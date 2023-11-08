package R.VD.goomong.search.model;

import R.VD.goomong.global.model.BaseDateEntity;
import R.VD.goomong.member.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE Search SET del_date = CURRENT_TIMESTAMP WHERE search_id = ?")
public class Search extends BaseDateEntity {

    @Id
    private Long searchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;

}