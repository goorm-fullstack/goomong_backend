package R.VD.goomong.like.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@Schema(description = "게시글 좋아요 버튼 클릭 시 정보")
public class RequestPostLikeDto {

    @Positive
    @Schema(description = "좋아요 버튼 클릭한 회원 id", example = "1", required = true)
    private Long memberId;

    @Positive
    @Schema(description = "좋아요 클릭된 게시글의 id", example = "1")
    private Long postId;
}
