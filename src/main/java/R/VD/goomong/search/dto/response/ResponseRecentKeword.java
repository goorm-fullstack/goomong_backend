package R.VD.goomong.search.dto.response;

import R.VD.goomong.search.model.Search;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRecentKeword {

    private String keyword;

    private LocalDateTime regDate;

    public ResponseRecentKeword(Search search) {
        this.keyword = search.getWord().getKeyword();
        this.regDate = search.getRegDate();
    }

}
