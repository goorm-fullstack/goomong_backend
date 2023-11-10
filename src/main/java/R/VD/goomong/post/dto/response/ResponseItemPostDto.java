package R.VD.goomong.post.dto.response;

import R.VD.goomong.comment.dto.response.ResponseCommentDto;
import R.VD.goomong.file.model.Files;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "판매/기부/교환 게시글 조회 정보")
public class ResponseItemPostDto {

    @Schema(description = "게시글 id", example = "1")
    private Long id;

    @Schema(description = "작성자 아이디", example = "test")
    private String memberId;

    @Schema(description = "상품 정보", implementation = ResponseItemDto.class)
    private ResponseItemDto item;

    @Schema(description = "게시글 이미지 정보", implementation = Image.class)
    private List<Image> imageList;

    @Schema(description = "게시글 파일 정보", implementation = Files.class)
    private List<Files> fileList;

    @Schema(description = "게시글 댓글 정보", implementation = ResponseCommentDto.class)
    private List<ResponseCommentDto> commentList;

    @Schema(description = "게시글 신고 id", example = "[1, 2]")
    private List<Long> reportIdList;

    @Schema(description = "게시글 종류", example = "판매")
    private String postType;

    @Schema(description = "게시글 제목", example = "제목입니다")
    private String postTitle;

    @Schema(description = "게시글 내용", example = "내용입니다")
    private String postContent;

    @Schema(description = "게시글 조회수", example = "0")
    private int postViews;

    @Schema(description = "게시글 좋아요수", example = "0")
    private int postLikeNo;

    @Schema(description = "게시글 생성 날짜", example = "2023-11-03T18:14:49.792+09:00")
    private String regDate;

    @Schema(description = "게시글 삭제 날짜", example = "2023-11-03T18:14:49.792+09:00")
    private String delDate;
}
