package R.VD.goomong.support.dto.response;

import R.VD.goomong.search.dto.PageInfo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ResponsePageWrap<T> {

    private T data;

    private PageInfo pageInfo;

}
