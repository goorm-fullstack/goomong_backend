package goomong.review.dto.request;

import goomong.member.model.Member;
import goomong.review.model.Review;
import lombok.Data;

@Data
public class RequestReviewDto {
    private Member member;//작성자
    private String content;//리뷰 내용
    private Float rate;//평점

    public Review toEntity() {
        return Review.builder()
                .member(member)
                .content(content)
                .rate(rate)
                .build();
    }
}
