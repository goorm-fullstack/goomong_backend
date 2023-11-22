package R.VD.goomong.item.dto.response;

import R.VD.goomong.global.model.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseItemPageDto<T> {
    private T data;
    private int pageNum;
}
