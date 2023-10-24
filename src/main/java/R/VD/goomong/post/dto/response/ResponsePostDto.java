package R.VD.goomong.post.dto.response;

import R.VD.goomong.comment.dto.response.ResponseCommentDto;
import R.VD.goomong.file.model.Files;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import lombok.*;

import java.time.LocalDateTime;
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
    private String postType;
    private String postTitle;
    private String postContent;
    private int postViews;
    private int postLikeNo;
    private LocalDateTime regDate;
    private LocalDateTime chgDate;
    private LocalDateTime delDate;
}
