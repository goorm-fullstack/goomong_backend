package R.VD.goomong.report.dto.response;

import R.VD.goomong.ask.dto.response.ResponseAskDto;
import R.VD.goomong.comment.dto.response.ResponseCommentDto;
import R.VD.goomong.file.model.Files;
import R.VD.goomong.review.dto.response.ResponseReviewDto;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponseReportDto {

    private Long id;
    private String memberId;
    private Long postId;
    private ResponseCommentDto comment;
    private ResponseReviewDto review;
    private ResponseAskDto ask;
    private List<Files> filesList;
    private String reportReason;
    private String reportCheck;
    private String reportResult;
    private ZonedDateTime regDate;
    private ZonedDateTime delDate;
}
