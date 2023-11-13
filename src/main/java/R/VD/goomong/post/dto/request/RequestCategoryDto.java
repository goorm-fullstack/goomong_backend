package R.VD.goomong.post.dto.request;

import R.VD.goomong.global.validation.EnumValue;
import R.VD.goomong.post.model.Category;
import R.VD.goomong.post.model.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Schema(description = "커뮤니티 및 FAQ 카테고리 생성 정보")
public class RequestCategoryDto {

    @EnumValue(enumClass = Type.class)
    @Schema(description = "어디에 생성할 카테고리인지 정합니다.(예: 커뮤니티 / FAQ)", implementation = Type.class, required = true)
    private String categoryGroup; // 카테고리 그룹(커뮤니티, FAQ)

    @NotBlank(message = "카테고리 이름을 입력해주세요.")
    @Schema(description = "카테고리 이름", example = "카테고리 이름입니다.")
    private String categoryName; // 카테고리 이름

    // RequestCategoryDto 엔티티화
    public Category toEntity() {
        return Category.builder()
                .categoryName(categoryName)
                .build();
    }
}
