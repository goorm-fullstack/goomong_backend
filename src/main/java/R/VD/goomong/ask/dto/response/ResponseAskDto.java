package R.VD.goomong.ask.dto.response;

import R.VD.goomong.file.model.Files;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponseAskDto {

    private Long id;
    private String memberId; // 작성자
    private ResponseItemDto item; // 작성할 아이템
    private List<Files> filesList; // 업로드 파일
    private String title; // 문의 제목
    private String content; // 문의 내용
    private List<ResponseAskDto> answerList; // 답변 내용들
    private ZonedDateTime regDate; // 생성 날짜
    private ZonedDateTime delDate; // 삭제 날짜
}
