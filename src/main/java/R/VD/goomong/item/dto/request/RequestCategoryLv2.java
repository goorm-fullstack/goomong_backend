package goomong.item.dto.request;

import goomong.item.model.ItemCategory;
import lombok.Data;

@Data
public class RequestCategoryLv2 {
    private String title;//카테고리 명
    private Long parentId;//부모 카테고리

    public ItemCategory toEntity() {
        return ItemCategory.builder()
                .title(title)
                .level(2)
                .build();
    }
}
