package R.VD.goomong.post.dto.request;

import R.VD.goomong.post.model.PostCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class RequestPostCategoryDto {

    @NotBlank(message = "카테고리 그룹을 입력해주세요.")
    private String postCategoryGroup; // 카테고리 그룹(커뮤니티, FAQ)

    @NotBlank(message = "카테고리 이름을 입력해주세요.")
    private String postCategoryName; // 카테고리 이름

    // RequestPostCategoryDto 엔티티화
    public PostCategory toEntity() {
        return PostCategory.builder()
                .postCategoryGroup(postCategoryGroup)
                .postCategoryName(postCategoryName)
                .build();
    }
}
