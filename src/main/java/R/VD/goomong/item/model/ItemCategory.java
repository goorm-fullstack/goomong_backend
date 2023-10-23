package R.VD.goomong.item.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//DB 인덱스
    private String title;//카테고리 명
    private int level;//계층 레벨 1 : 대분류, 2 : 소분류

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int order;//순서값

    @ManyToOne
    private ItemCategory parent;//부모 카테고리

    @OneToMany
    private List<ItemCategory> childCategory = new ArrayList<>();//자식 카테고리

    public void setParent(ItemCategory itemCategory) {
        this.parent = itemCategory;
    }
}
