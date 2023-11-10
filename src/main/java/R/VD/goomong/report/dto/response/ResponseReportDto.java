package R.VD.goomong.report.dto.response;

import R.VD.goomong.ask.dto.response.ResponseAskDto;
import R.VD.goomong.comment.dto.response.ResponseCommentDto;
import R.VD.goomong.file.model.Files;
import R.VD.goomong.post.dto.response.ResponsePostDto;
import R.VD.goomong.review.dto.response.ResponseReviewDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponseReportDto {

    private Long id;
    private String memberId;
    private ResponsePostDto post;
    private ResponseCommentDto comment;
    private ResponseReviewDto review;
    private ResponseAskDto ask;
    private List<Files> filesList;
    private String reportReason;
    private String reportCheck;
    private String reportResult;
    private LocalDateTime regDate;
    private LocalDateTime delDate;
}
