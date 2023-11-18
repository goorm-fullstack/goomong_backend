package R.VD.goomong.report.dto.response;

import R.VD.goomong.file.model.Files;
import R.VD.goomong.global.model.PageInfo;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "조회한 신고 정보")
public class ResponseReportDto {

    @Schema(description = "조회한 신고글 id", example = "1")
    private Long id;

    @Schema(description = "신고글 작성자 아이디", example = "아이디")
    private String memberId;

    @Schema(description = "신고당한 게시글 id", example = "1")
    private Long postId;

    @Schema(description = "신고당한 댓글 id", example = "1")
    private Long commentId;

    @Schema(description = "신고당한 리뷰 id", example = "1")
    private Long reviewId;

    @Schema(description = "신고당한 문의글 id", example = "1")
    private Long askId;

    @ArraySchema(schema = @Schema(description = "신고 시 업로드한 파일 리스트", implementation = Files.class))
    private List<Files> filesList;

    @Schema(description = "신고 사유", example = "신고 이유입니다")
    private String reportReason;

    @Schema(description = "신고 처리 현황, 처리 중/처리 완료", example = "처리 중")
    private String reportCheck;

    @Schema(description = "신고글 처리 결과, 이상 없음/삭제 처리, 처리 중일 시 null", example = "이상 없음")
    private String reportResult;

    @Schema(description = "신고글 생성 날짜", example = "2023-11-03T18:14:49.792+09:00")
    private LocalDateTime regDate;

    @Schema(description = "신고글 삭제 날짜", example = "2023-11-03T18:14:49.792+09:00")
    private LocalDateTime delDate;

    @Schema(description = "페이징 정보", implementation = PageInfo.class)
    private PageInfo pageInfo;
}