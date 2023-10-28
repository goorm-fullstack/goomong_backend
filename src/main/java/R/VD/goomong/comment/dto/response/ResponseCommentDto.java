package R.VD.goomong.comment.dto.response;

import R.VD.goomong.report.dto.response.ResponseReportDto;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponseCommentDto {

    private Long id;
    private String memberId;
    private List<ResponseCommentDto> childrenComment;
    private List<ResponseReportDto> reportList;
    private String content;
    private int likeNo;
    private ZonedDateTime regDate;
    private ZonedDateTime delDate;
}
