package R.VD.goomong.global.model;

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