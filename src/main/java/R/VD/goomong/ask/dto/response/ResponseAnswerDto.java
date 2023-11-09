package R.VD.goomong.ask.dto.response;

import R.VD.goomong.file.model.Files;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Schema(description = "답변글 조회 정보")
public class ResponseAnswerDto {

    @Schema(description = "답변글 id", example = "1")
    private Long id;

    @Schema(description = "작성자 id", example = "test")
    private String memberId;

    @ArraySchema(schema = @Schema(description = "업로드된 파일 리스트", implementation = Files.class))
    private List<Files> filesList;

    @Schema(description = "답변글 제목", example = "제목입니다.")
    private String title;

    @Schema(description = "답변글 내용", example = "내용입니다.")
    private String content;

    @Schema(description = "작성 날짜", example = "2023-11-03T18:14:49.792+09:00")
    private ZonedDateTime regDate; // 생성 날짜

    @Schema(description = "삭제 날짜", example = "2023-11-03T18:14:49.792+09:00")
    private ZonedDateTime delDate; // 삭제 날짜
}
