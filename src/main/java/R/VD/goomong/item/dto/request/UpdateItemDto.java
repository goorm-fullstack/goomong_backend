package R.VD.goomong.item.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateItemDto {
    private Long id;//수정할 아이템 아이디
    private int price;
    private String title;
    private String description;
    private List<Long> itemCategories;
}
