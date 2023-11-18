package R.VD.goomong.comment.dto.request;

import R.VD.goomong.comment.model.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Schema(description = "댓글 등록 정보")
public class RequestCommentDto {

    @Positive
    @Schema(description = "작성자 id", example = "1", required = true)
    private Long memberId;

    @Positive
    @Schema(description = "댓글이 작성될 게시글 id", example = "1")
    private Long postId;

    @Positive
    @Schema(description = "댓글이 작성될 리뷰 id", example = "1")
    private Long reviewId;

    @Schema(description = "대댓글 작성 시 상위 댓글의 id", example = "1")
    private Long parentCommentId;

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    @Schema(description = "댓글 내용", example = "내용입니다.", required = true)
    private String content;

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .build();
    }
}
