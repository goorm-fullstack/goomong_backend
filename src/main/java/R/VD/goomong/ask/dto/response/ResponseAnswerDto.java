package R.VD.goomong.ask.dto.response;

import R.VD.goomong.file.model.Files;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class ResponseAnswerDto {

    private Long id;
    private String memberId;
    private List<Files> filesList;
    private String title;
    private String content;
    private LocalDateTime regDate; // 생성 날짜
    private LocalDateTime delDate; // 삭제 날짜
}
