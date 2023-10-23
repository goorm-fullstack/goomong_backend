package goomong.review.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import goomong.image.model.Image;
import goomong.item.model.Item;
import goomong.member.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//DB 인덱스

    @ManyToOne
    @JsonIgnore
    private Member member;//작성자

    @ManyToOne
    @JsonIgnore
    private Item item;//리뷰를 남길 아이템

    @OneToMany
    private List<Image> imageList = new ArrayList<>();

    private String content;//리뷰 내용
    private Float rate;//평점

    public void setItem(Item item) {
        this.item = item;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }
}
