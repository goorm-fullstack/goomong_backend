package R.VD.goomong.post.dto.response;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.post.model.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "카테고리 조회 정보")
public class ResponseCategoryDto {

    @Schema(description = "카테고리 id", example = "1")
    private Long id;

    @Schema(description = "카테고리 이미지", implementation = Image.class)
    private Image image;

    @Schema(description = "카테고리 그룹", example = "COMMUNITY")
    @Enumerated(EnumType.STRING)
    private Type categoryGroup;

    @Schema(description = "카테고리 이름", example = "카테고리 이름")
    private String categoryName;

    @Schema(description = "카테고리 생성 날짜", example = "2023-11-03T18:14:49.792+09:00")
    private LocalDateTime regDate;

    @Schema(description = "카테고리 삭제 날짜", example = "2023-11-03T18:14:49.792+09:00")
    private LocalDateTime delDate;
}
