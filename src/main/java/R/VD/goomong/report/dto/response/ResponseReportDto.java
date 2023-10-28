package R.VD.goomong.report.dto.response;

import R.VD.goomong.comment.dto.response.ResponseCommentDto;
import R.VD.goomong.file.model.Files;
import R.VD.goomong.post.dto.response.ResponsePostDto;
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
    private ResponsePostDto post;
    private ResponseCommentDto comment;
    private List<Files> filesList;
    private String reportReason;
    private String reportCheck;
    private String reportResult;
    private ZonedDateTime regDate;
    private ZonedDateTime delDate;
}
