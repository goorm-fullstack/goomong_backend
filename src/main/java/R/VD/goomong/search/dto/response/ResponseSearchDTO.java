package R.VD.goomong.search.dto.response;

import R.VD.goomong.global.model.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ResponseSearchDTO<T> {

    private T data;
    private PageInfo pageInfo;

}
