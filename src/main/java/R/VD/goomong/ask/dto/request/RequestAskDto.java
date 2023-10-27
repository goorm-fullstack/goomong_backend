package R.VD.goomong.ask.dto.request;

import R.VD.goomong.ask.model.Ask;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

// 문의 작성 Dto입니다.
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class RequestAskDto {

    @Positive
    private Long memberId;

    @Positive
    private Long itemId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    // 엔티티화
    public Ask toEntity() {
        return Ask.builder()
                .title(title)
                .content(content)
                .build();
    }
}
