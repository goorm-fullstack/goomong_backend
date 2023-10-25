package R.VD.goomong.comment.dto.request;

import R.VD.goomong.comment.model.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestCommentDto {

    @Positive
    private Long memberId;

    @Positive
    private Long postId;

    private Long parentCommentId;

    @NotBlank
    private String content;

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .regDate(LocalDateTime.now())
                .build();
    }
}
