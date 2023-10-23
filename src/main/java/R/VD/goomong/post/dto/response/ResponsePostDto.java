package R.VD.goomong.post.dto.response;

import R.VD.goomong.item.dto.response.ResponseItemDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponsePostDto {

    private Long id;
    private String member;
    private ResponseItemDto item;
    private String postType;
    private String postName;
    private String postContent;
    private int postViews;
    private int postLikeNo;
    private LocalDateTime regDate;
}
