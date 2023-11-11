package R.VD.goomong.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "QnA 조회 정보")
public class ResponseQuestionDto {

    @Schema(description = "조회한 질문 id", example = "1")
    private Long id;

    @Schema(description = "조회한 질문의 답변", implementation = ResponseAnswerForQuestionDto.class)
    private ResponseAnswerForQuestionDto children;
    private String title;
    private String content;
}
