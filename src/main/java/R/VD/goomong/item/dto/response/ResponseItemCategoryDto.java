package R.VD.goomong.item.dto.response;

import R.VD.goomong.item.model.ItemCategory;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseItemCategoryDto {
    private Long id;//DB 인덱스
    private String title;//카테고리 명
    private int level;//계층 레벨 1 : 대분류, 2 : 소분류
    private int order;//순서값
    private ItemCategory parent;//부모 카테고리
    private List<ItemCategory> childCategory = new ArrayList<>();//자식 카테고리

    public ResponseItemCategoryDto(ItemCategory itemCategory) {
        this.id = itemCategory.getId();
        this.title = itemCategory.getTitle();
        this.level = itemCategory.getLevel();
        this.order = itemCategory.getOrder();
        this.parent = itemCategory.getParent();
        this.childCategory = itemCategory.getChildCategory();
    }
}
