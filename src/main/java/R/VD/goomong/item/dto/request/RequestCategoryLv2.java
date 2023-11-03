package R.VD.goomong.item.dto.request;

import R.VD.goomong.item.model.ItemCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RequestCategoryLv2 {
    @NotNull
    @NotEmpty
    private String title;//카테고리 명

    @Positive
    private Long parentId;//부모 카테고리

    public ItemCategory toEntity() {
        return ItemCategory.builder()
                .title(title)
                .level(2)
                .build();
    }
}
