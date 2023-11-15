package R.VD.goomong.post.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponsePostCategoryDto {

    private Long id;
    private String postCategoryName;
    private LocalDateTime regDate;
    private LocalDateTime delDate;
}
