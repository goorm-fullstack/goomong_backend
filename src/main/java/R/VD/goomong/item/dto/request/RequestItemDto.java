package R.VD.goomong.item.dto.request;

import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.model.ItemCategory;
import lombok.Data;

import java.util.List;

@Data
public class RequestItemDto {
    private String title;//제목
    private int price;//가격
    private String describe;//설명
    private List<ItemCategory> itemCategories;//카테고리 목록

    public Item toEntity() {
        return Item.builder()
                .title(title)
                .price(price)
                .describe(describe)
                .itemCategories(itemCategories)
                .build();
    }
}
