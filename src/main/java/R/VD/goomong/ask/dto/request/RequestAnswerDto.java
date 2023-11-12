package R.VD.goomong.ask.dto.request;

import R.VD.goomong.ask.model.Ask;
import lombok.Data;

// 문의에 대한 답변 Dto입니다.
@Data
public class RequestAnswerDto {
    private Long itemId;
    private String title;
    private String content;
    private Long parentId;

    public Ask toEntity() {
        return Ask.builder()
                .title(title)
                .content(content)
                .build();
    }
}
