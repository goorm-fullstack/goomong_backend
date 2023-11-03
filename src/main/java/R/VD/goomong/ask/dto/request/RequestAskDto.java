package R.VD.goomong.ask.dto.request;

import R.VD.goomong.ask.model.Ask;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

// 문의 작성 Dto입니다.
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Schema(description = "문의글 정보")
public class RequestAskDto {

    @Positive
    @Schema(description = "작성자 id", example = "1", required = true)
    private Long memberId;

    @Positive
    @Schema(description = "상품 id", example = "1", required = true)
    private Long itemId;

    @NotBlank
    @Schema(description = "문의글 제목", example = "제목입니다.")
    private String title;

    @NotBlank
    @Schema(description = "문의글 내용", example = "내용입니다.")
    private String content;

    // 엔티티화
    public Ask toEntity() {
        return Ask.builder()
                .title(title)
                .content(content)
                .build();
    }
}
