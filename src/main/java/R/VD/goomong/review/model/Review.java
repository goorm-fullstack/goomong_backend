package R.VD.goomong.review.model;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.model.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//DB 인덱스

    @ManyToOne
    private Member member;//작성자

    @ManyToOne
    private Item item;//리뷰를 남길 아이템

    @OneToMany
    private List<Image> imageList = new ArrayList<>();

    private String content;//리뷰 내용
    private Float rate;//평점
}
