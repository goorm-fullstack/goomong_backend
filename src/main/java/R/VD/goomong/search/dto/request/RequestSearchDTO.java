package R.VD.goomong.search.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSearchDTO {
    private int page;
    private int pageSize;
    private Long memberId;
    private String keyword;
    private String order;
    private String category;
}