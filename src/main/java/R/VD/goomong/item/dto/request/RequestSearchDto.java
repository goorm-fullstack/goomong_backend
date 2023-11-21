package R.VD.goomong.item.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestSearchDto {
    private int page;
    private int pageSize;
}
