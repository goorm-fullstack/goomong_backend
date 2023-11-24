package R.VD.goomong.search.dto.response;

import R.VD.goomong.search.model.Search;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRecentKeword {

    private Long searchId;

    private String keyword;

    public ResponseRecentKeword(Search search) {
        this.searchId = search.getSearchId();
        this.keyword = search.getWord().getKeyword();
    }

}
