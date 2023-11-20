package R.VD.goomong.search.dto.request;

import R.VD.goomong.post.model.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSearchDTO {
    private String keyword;
    private String order;
    private String categoryTitle;
}
