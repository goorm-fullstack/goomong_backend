package R.VD.goomong.review.dto.request;

import R.VD.goomong.review.model.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@Schema(description = "리뷰 작성 정보")
public class RequestReviewDto {

    @Positive
    @Schema(description = "작성자 id", example = "1", required = true)
    private Long memberId;

    @Positive
    @Schema(description = "리뷰 작성할 상품 id", example = "1", required = true)
    private Long itemId;

    @NotBlank
    @Schema(description = "리뷰 제목", example = "제목입니다")
    private String title;

    @NotBlank
    @Schema(description = "리뷰 내용", example = "내용입니다")
    private String content;

    @PositiveOrZero
    @Schema(description = "리뷰 평점", example = "5.0", required = true)
    private Float rate;

    public Review toEntity() {
        return Review.builder()
                .title(title)
                .content(content)
                .rate(rate)
                .build();
    }
}
