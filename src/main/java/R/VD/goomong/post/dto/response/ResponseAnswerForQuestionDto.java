package R.VD.goomong.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "답변 조회 정보")
public class ResponseAnswerForQuestionDto {

    private Long id;
    private String content;
}
