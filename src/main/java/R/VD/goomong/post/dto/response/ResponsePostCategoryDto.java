package R.VD.goomong.post.dto.response;

import lombok.*;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponsePostCategoryDto {

    private Long id;
    private String postCategoryName;
}
