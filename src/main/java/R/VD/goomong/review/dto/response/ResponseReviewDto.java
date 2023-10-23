package R.VD.goomong.review.dto.response;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.review.model.Review;
import lombok.Data;

import java.util.List;

@Data
public class ResponseReviewDto {
    private Long id;//DB 인덱스
    private Member member;//작성자
    private List<Image> imageList;
    private String content;//리뷰 내용
    private Float rate;//평점

    public ResponseReviewDto(Review review) {
        this.id = review.getId();
        this.member = review.getMember();
        this.imageList = review.getImageList();
        this.content = review.getContent();
        this.rate = review.getRate();
    }
}
