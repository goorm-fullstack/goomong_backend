package R.VD.goomong.post.dto.request;

import R.VD.goomong.post.model.PostCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Schema(description = "커뮤니티 및 FAQ 카테고리 생성 정보")
public class RequestPostCategoryDto {

    @NotBlank(message = "카테고리 그룹을 입력해주세요.")
    @Schema(description = "어디에 생성할 카테고리인지 정합니다.(예: 커뮤니티 / FAQ)", example = "커뮤니티")
    private String postCategoryGroup; // 카테고리 그룹(커뮤니티, FAQ)

    @NotBlank(message = "카테고리 이름을 입력해주세요.")
    @Schema(description = "카테고리 이름", example = "카테고리 이름입니다.")
    private String postCategoryName; // 카테고리 이름

    // RequestPostCategoryDto 엔티티화
    public PostCategory toEntity() {
        return PostCategory.builder()
                .postCategoryGroup(postCategoryGroup)
                .postCategoryName(postCategoryName)
                .build();
    }
}
