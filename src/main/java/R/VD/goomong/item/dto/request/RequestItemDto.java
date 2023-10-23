package goomong.item.dto.request;

import goomong.item.model.Item;
import lombok.Data;

import java.util.List;

@Data
public class RequestItemDto {
    private String title;//제목
    private int price;//가격
    private String describe;//설명
    private List<Long> itemCategories;//카테고리 목록

    public Item toEntity() {
        return Item.builder()
                .title(title)
                .price(price)
                .describe(describe)
                .build();
    }
}
