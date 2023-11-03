package R.VD.goomong.search.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PageInfo {

    private int page;
    private int size;
    private int totalElements;
    private int totalPage;

}