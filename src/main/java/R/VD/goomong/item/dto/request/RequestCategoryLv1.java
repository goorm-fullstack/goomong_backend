package goomong.item.dto.request;

import goomong.item.model.ItemCategory;
import lombok.Data;

@Data
public class RequestCategoryLv1 {
    private String title;//카테고리 명

    public ItemCategory toEntity() {
        return ItemCategory.builder()
                .level(1)
                .title(title)
                .build();
    }
}
