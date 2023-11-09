package R.VD.goomong.post.dto.response;

import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponsePostCategoryDto {

    private Long id;
    private String postCategoryName;
    private ZonedDateTime regDate;
    private ZonedDateTime delDate;
}
