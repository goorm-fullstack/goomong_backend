package R.VD.goomong.search.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PageInfo {

    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPage;

}