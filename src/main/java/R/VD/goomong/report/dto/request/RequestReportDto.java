package R.VD.goomong.report.dto.request;

import R.VD.goomong.report.model.Report;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestReportDto {

    @Positive
    private Long memberId;

    private Long postId;

    private Long commentId;

    @NotBlank(message = "신고 이유를 적어주세요.")
    private String reportReason;

    public Report toEntity() {
        return Report.builder()
                .reportReason(reportReason)
                .reportCheck("처리 중")
                .build();
    }
}
