package R.VD.goomong.search.dto.request;

import R.VD.goomong.post.model.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPostSearchDTO {
    private int page;
    private int pageSize;
    private Long memberId;
    private String keyword;
    private String order;
    private Category category;
}
