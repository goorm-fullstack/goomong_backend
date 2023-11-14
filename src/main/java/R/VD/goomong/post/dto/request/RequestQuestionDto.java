package R.VD.goomong.post.dto.request;

import R.VD.goomong.post.model.Qna;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@Schema(description = "질문 작성 request")
public class RequestQuestionDto {

    @NotBlank
    @Schema(description = "질문 제목", example = "제목입니다")
    private String title;

    @NotBlank
    @Schema(description = "질문 내용", example = "내용입니다")
    private String content;

    public Qna toEntity() {
        return Qna.builder()
                .title(title)
                .content(content)
                .build();
    }
}
