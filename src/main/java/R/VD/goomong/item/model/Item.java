package R.VD.goomong.item.model;

import R.VD.goomong.ask.model.Ask;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.review.model.Review;
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
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//DB 인덱스
    private String title;//제목

    @ManyToOne
    private Member member;//작성자

    private int price;//가격

    @OneToMany
    private List<Image> thumbNailList = new ArrayList<>();//썸네일 리스트

    @Lob//긴 문자열 저장을 위한 어노테이션
    private String describe;//설명

    @OneToMany
    private List<ItemCategory> itemCategories = new ArrayList<>();//카테고리 목록

    @OneToMany
    private List<Review> reviewList = new ArrayList<>();//리뷰 목록

    @OneToMany
    private List<Ask> askList = new ArrayList<>();

    private Float rate;//평점

    public void setThumbNailList(List<Image> thumbNailList) {
        this.thumbNailList = thumbNailList;
    }
}
