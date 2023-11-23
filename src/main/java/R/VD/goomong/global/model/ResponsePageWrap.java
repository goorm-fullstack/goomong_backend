package R.VD.goomong.global.model;

import R.VD.goomong.global.model.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePageWrap<T> {

    private T data;

    private PageInfo pageInfo;

}