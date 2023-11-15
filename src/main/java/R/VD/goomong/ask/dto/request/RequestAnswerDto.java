package R.VD.goomong.ask.dto.request;

import R.VD.goomong.ask.model.Ask;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

// 문의에 대한 답변 Dto입니다.
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Schema(description = "답변글 정보")
public class RequestAnswerDto {

    @Positive
    @Schema(description = "작성자 id", required = true)
    private Long memberId;

    @NotBlank
    @Schema(description = "답변글 제목")
    private String title;

    @NotBlank
    @Schema(description = "답변글 내용")
    private String content;

    @Positive
    @Schema(description = "답변하고자 하는 문의글의 id", required = true)
    private Long parentId;

    // 엔티티화
    public Ask toEntity() {
        return Ask.builder()
                .title(title)
                .content(content)
                .build();
    }
}