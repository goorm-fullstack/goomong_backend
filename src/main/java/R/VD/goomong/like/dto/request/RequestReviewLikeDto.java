package R.VD.goomong.like.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@Schema(description = "리뷰 좋아요 버튼 클릭 시 정보")
public class RequestReviewLikeDto {

    @Positive
    @Schema(description = "좋아요 버튼 클릭한 회원 id", example = "1", required = true)
    private Long memberId;

    @Positive
    @Schema(description = "좋아요 클릭된 리뷰의 id", example = "1")
    private Long reviewId;
}
