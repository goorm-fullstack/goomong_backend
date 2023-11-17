package R.VD.goomong.like.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "좋아요 클릭한 정보")
public class ResponseLikeDto {

    private Long id;
    private Long postId;
    private Long commentId;
    private Long reviewId;
}
