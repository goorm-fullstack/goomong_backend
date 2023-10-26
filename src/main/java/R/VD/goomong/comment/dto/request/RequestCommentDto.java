package R.VD.goomong.comment.dto.request;

import R.VD.goomong.comment.model.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class RequestCommentDto {

    @Positive
    private Long memberId;

    @Positive
    private Long postId;

    private Long parentCommentId;

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .build();
    }
}
