package R.VD.goomong.search.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestSearchDTO {
    private Long memberId;
    private String keyword;
    private String order;
    private String categoryTitle;
    private int page;
    private int size;
}
