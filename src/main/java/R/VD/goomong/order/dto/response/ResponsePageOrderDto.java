package R.VD.goomong.order.dto.response;

import R.VD.goomong.global.model.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponsePageOrderDto<T> {
    T data;
    PageInfo pageInfo;
}
