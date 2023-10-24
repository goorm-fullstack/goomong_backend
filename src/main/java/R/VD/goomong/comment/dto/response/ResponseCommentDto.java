package R.VD.goomong.comment.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponseCommentDto {

    private Long id;
    private String memberId;
    private String content;
    private int likeNo;
    private LocalDateTime regDate;
    private LocalDateTime chgDate;
    private LocalDateTime delDate;
}
