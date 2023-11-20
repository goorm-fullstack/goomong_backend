package R.VD.goomong.post.dto.response;

import R.VD.goomong.global.model.PageInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "QnA 조회 정보")
public class ResponseQuestionDto {

    @Schema(description = "조회한 질문 id", example = "1")
    private Long id;

    @Schema(description = "조회한 질문 카테고리", example = "카테고리 이름")
    private String categoryName;

    @Schema(description = "조회한 질문의 답변", implementation = ResponseAnswerForQuestionDto.class)
    private ResponseAnswerForQuestionDto children;

    @Schema(description = "조회한 질문 제목", example = "제목입니다")
    private String title;

    @Schema(description = "조회한 질문 내용", example = "내용입니다")
    private String content;

    @Schema(description = "생성 날짜", implementation = LocalDateTime.class)
    private LocalDateTime regDate;

    @Schema(description = "삭제 날짜", implementation = LocalDateTime.class)
    private LocalDateTime delDate;

    @Schema(description = "페이징 정보", implementation = PageInfo.class)
    private PageInfo pageInfo;
}
