package R.VD.goomong.item.dto.request;

import R.VD.goomong.item.model.ItemCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestCategoryLv1 {
    @NotNull
    @NotEmpty
    private String title;//카테고리 명

    public ItemCategory toEntity() {
        return ItemCategory.builder()
                .level(1)
                .title(title)
                .build();
    }
}
