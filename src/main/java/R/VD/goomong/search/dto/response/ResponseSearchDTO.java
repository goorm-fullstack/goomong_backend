package R.VD.goomong.search.dto.response;

import R.VD.goomong.search.dto.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseSearchDTO<T> {

    private T data;
    private PageInfo pageInfo;

}
