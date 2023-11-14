package R.VD.goomong.post.dto.request;

import R.VD.goomong.post.model.Qna;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@Schema(description = "질문에 대한 답변 request")
public class RequestAnswerForQuestionDto {

    @Positive
    @Schema(description = "답변 작성할 질문 id", example = "1", required = true)
    private Long parentId;

    @NotBlank
    @Schema(description = "답변 내용", example = "내용입니다")
    private String content;

    public Qna toEntity() {
        return Qna.builder()
                .content(content)
                .build();
    }
}
