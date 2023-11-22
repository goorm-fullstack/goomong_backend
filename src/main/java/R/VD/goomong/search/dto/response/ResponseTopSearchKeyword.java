package R.VD.goomong.search.dto.response;

import R.VD.goomong.search.model.Word;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTopSearchKeyword {

    private String keyword;

    private Long wordCount;

    public ResponseTopSearchKeyword(Word word) {
        this.keyword = word.getKeyword();
        this.wordCount = word.getWordCount();
    }

}
