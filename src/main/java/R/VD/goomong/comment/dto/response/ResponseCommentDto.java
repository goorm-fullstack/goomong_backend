package R.VD.goomong.comment.dto.response;

import R.VD.goomong.global.model.PageInfo;
import R.VD.goomong.image.model.Image;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "조회된 댓글")
public class ResponseCommentDto {

    @Schema(description = "댓글 id", example = "1")
    private Long id;

    @Schema(description = "작성자 회원 id", example = "아이디")
    private String memberId;

    @Schema(description = "댓글 달린 게시글 제목", example = "게시글 제목")
    private String postTitle;

    @Schema(description = "댓글 달린 게시글 id", example = "1")
    private Long postId;

    @Schema(description = "작성자 이미지", implementation = Image.class)
    private List<Image> memberImageList;

    @ArraySchema(schema = @Schema(description = "대댓글 리스트", implementation = ResponseCommentDto.class))
    private List<ResponseCommentDto> childrenComment;

    @ArraySchema(schema = @Schema(description = "신고 id 리스트", implementation = Long.class))
    private List<Long> reportIdList;

    @Schema(description = "부모 댓글 id", example = "1")
    private Long parentId;

    @Schema(description = "댓글 내용", example = "내용입니다.")
    private String content;

    @Schema(description = "좋아요 수", example = "1")
    private int likeNo;

    @Schema(description = "생성 날짜", example = "2023-11-03T18:14:49.792+09:00")
    private LocalDateTime regDate;

    @Schema(description = "삭제 날짜", example = "2023-11-03T18:14:49.792+09:00")
    private LocalDateTime delDate;

    @Schema(description = "페이징 정보", implementation = PageInfo.class)
    private PageInfo pageInfo;
}