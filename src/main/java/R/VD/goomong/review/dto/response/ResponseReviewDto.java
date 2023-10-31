package R.VD.goomong.review.dto.response;

import R.VD.goomong.image.model.Image;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponseReviewDto {
    private Long id;//DB 인덱스
    private String memberId;//작성자
    private List<Image> imageList;
    private String title;
    private String content;//리뷰 내용
    private Float rate;//평점
    private ZonedDateTime regDate;
    private ZonedDateTime delDate;
}
