package R.VD.goomong.item.model;

import R.VD.goomong.global.model.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class ItemCategory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//DB 인덱스
    private String title;//카테고리 명
    private int level;//계층 레벨 1 : 대분류, 2 : 소분류

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int priority;//순서값

    @ManyToOne
    @JsonIgnore
    private ItemCategory parent;//부모 카테고리

    @OneToMany
    @JsonIgnore
    private List<ItemCategory> childCategory = new ArrayList<>();//자식 카테고리

    public void setParent(ItemCategory itemCategory) {
        this.parent = itemCategory;
    }
}
