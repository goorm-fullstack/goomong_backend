package R.VD.goomong.search.dto.response;

import R.VD.goomong.search.model.Word;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTopSearchKeyword {

    private String keyword;

    public ResponseTopSearchKeyword(Word word) {
        this.keyword = word.getKeyword();
    }

}
