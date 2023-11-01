package R.VD.goomong.item.dto.request;

import R.VD.goomong.item.model.Item;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class RequestItemDto {
    @NotEmpty
    @NotNull
    private String title;//제목
    
    @Positive
    private int price;//가격

    @NotEmpty
    @NotNull
    private String describe;//설명

    @NotEmpty
    private List<Long> itemCategories;//카테고리 목록

    public Item toEntity() {
        return Item.builder()
                .title(title)
                .price(price)
                .describe(describe)
                .build();
    }
}
