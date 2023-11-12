package R.VD.goomong.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "답변 조회 정보")
public class ResponseAnswerForQuestionDto {

    @Schema(description = "조회한 답변 id", example = "1")
    private Long id;

    @Schema(description = "조회한 답변 내용", example = "내용입니다")
    private String content;

    @Schema(description = "생성 날짜", implementation = LocalDateTime.class)
    private LocalDateTime regDate;

    @Schema(description = "삭제 날짜", implementation = LocalDateTime.class)
    private LocalDateTime delDate;
}
