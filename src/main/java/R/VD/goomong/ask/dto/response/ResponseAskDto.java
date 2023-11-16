package R.VD.goomong.ask.dto.response;

import R.VD.goomong.file.model.Files;
import R.VD.goomong.global.model.PageInfo;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "조회된 문의글")
public class ResponseAskDto {

    @Schema(description = "문의글 id", example = "1")
    private Long id;

    @Schema(description = "작성자 id", example = "test")
    private String memberId; // 작성자

    @Schema(description = "문의하고자 하는 상품", implementation = ResponseItemDto.class)
    private ResponseItemDto item; // 작성할 아이템

    @ArraySchema(schema = @Schema(description = "업로드한 파일 리스트", implementation = Files.class))
    private List<Files> filesList; // 업로드 파일

    @ArraySchema(schema = @Schema(description = "신고 id 리스트", implementation = Long.class))
    private List<Long> reportListId; // 신고

    @Schema(description = "문의글 제목", example = "제목입니다.")
    private String title; // 문의 제목

    @Schema(description = "문의글 내용", example = "내용입니다.")
    private String content; // 문의 내용

    @ArraySchema(schema = @Schema(description = "답변 리스트", implementation = ResponseAnswerDto.class))
    private List<ResponseAnswerDto> answerList; // 답변 내용들

    @Schema(description = "작성 날짜", example = "2023-11-03T18:14:49.792+09:00")
    private LocalDateTime regDate; // 생성 날짜

    @Schema(description = "삭제 날짜", example = "2023-11-03T18:14:49.792+09:00")
    private LocalDateTime delDate; // 삭제 날짜

    @Schema(description = "페이징 정보", implementation = PageInfo.class)
    private PageInfo pageInfo;
}