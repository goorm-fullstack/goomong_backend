package R.VD.goomong.search.model;

import R.VD.goomong.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE Word SET del_date = CURRENT_TIMESTAMP WHERE word_id = ?")
public class Word extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordId;

    @Column(nullable = false)
    private String keyword;

    @ColumnDefault("1")
    private Long wordCount;
    
    private LocalDateTime delDate;

    public Word(String keyword) {
        this.keyword = keyword;
    }

}