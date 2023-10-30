package R.VD.goomong.review.dto.request;

import R.VD.goomong.review.model.Review;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class RequestReviewDto {

    @Positive
    private Long memberId;

    @Positive
    private Long itemId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @PositiveOrZero
    private Float rate;

    public Review toEntity() {
        return Review.builder()
                .title(title)
                .content(content)
                .rate(rate)
                .build();
    }
}
