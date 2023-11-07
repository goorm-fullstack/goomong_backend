package R.VD.goomong.item.dto.request;

import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.model.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RequestNonSaleItemDto {
    @NotEmpty
    @NotNull
    private String title;//제목

    @NotEmpty
    @NotNull
    private String describe;//설명

    @NotEmpty
    @NotNull
    private Status status;//아이템 상태값

    @NotEmpty
    private List<Long> itemCategories;//카테고리 목록

    public Item toEntity() {
        return Item.builder()
                .title(title)
                .describe(describe)
                .status(status)
                .build();
    }
}