package R.VD.goomong.post.dto.response;

import R.VD.goomong.comment.dto.response.ResponseCommentDto;
import R.VD.goomong.file.model.Files;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.report.dto.response.ResponseReportDto;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponsePostDto {

    private Long id;
    private String member;
    private ResponseItemDto item;
    private ResponsePostCategoryDto postCategory;
    private List<Image> imageList;
    private List<Files> fileList;
    private List<ResponseCommentDto> commentList;
    private List<ResponseReportDto> report;
    private String postType;
    private String postTitle;
    private String postContent;
    private int postViews;
    private int postLikeNo;
    private ZonedDateTime regDate;
    private ZonedDateTime delDate;
}
