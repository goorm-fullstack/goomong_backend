package R.VD.goomong.item.dto.request;

import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.model.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RequestItemDto {
    @NotEmpty
    @NotNull
    private String title;//제목

    @NotEmpty
    @NotNull
    private String description;//설명

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;//아이템 상태값

    private int price;

    private Long memberId;

    @NotEmpty
    private List<Long> itemCategories;//카테고리 목록

    public Item toEntity() {
        return Item.builder()
                .title(title)
                .price(price)
                .description(description)
                .status(status)
                .build();
    }
}
