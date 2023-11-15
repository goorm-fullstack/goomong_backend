package R.VD.goomong.report.dto.request;

import R.VD.goomong.report.model.Report;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Schema(description = "신고 작성 정보")
public class RequestReportDto {

    @Positive
    @Schema(description = "작성자 id", example = "1", required = true)
    private Long memberId;

    @Schema(description = "신고할 게시글 id", example = "1")
    private Long postId;

    @Schema(description = "신고할 댓글 id", example = "1")
    private Long commentId;

    @Schema(description = "신고할 리뷰 id", example = "1")
    private Long reviewId;

    @Schema(description = "신고할 문의글 id", example = "1")
    private Long askId;

    @Schema(description = "신고 사유", example = "신고 이유입니다")
    @NotBlank(message = "신고 이유를 적어주세요.")
    private String reportReason;

    public Report toEntity() {
        return Report.builder()
                .reportReason(reportReason)
                .reportCheck("처리 중")
                .build();
    }
}
