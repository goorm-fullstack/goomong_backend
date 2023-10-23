package R.VD.goomong.ask.dto.request;

import R.VD.goomong.ask.model.Ask;
import lombok.Data;

// 문의 작성 Dto입니다.
@Data
public class RequestAskDto {
    private Long memberId;
    private Long itemId;
    private String title;
    private String content;

    public Ask toEntity() {
        return Ask.builder()
                .title(title)
                .content(content)
                .build();
    }
}
