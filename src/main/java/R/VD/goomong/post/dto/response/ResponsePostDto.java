package R.VD.goomong.post.dto.response;

import R.VD.goomong.comment.dto.response.ResponseCommentDto;
import R.VD.goomong.file.model.Files;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.post.model.Type;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "FAQ / 커뮤니티 게시글 조회 정보")
public class ResponsePostDto {

    @Schema(description = "게시글 id", example = "1")
    private Long id;

    @Schema(description = "작성자 아이디", example = "아이디")
    private String memberId;

    @ArraySchema(schema = @Schema(description = "게시글 이미지 리스트", implementation = Image.class))
    private List<Image> imageList;

    @ArraySchema(schema = @Schema(description = "게시글 파일 리스트", implementation = Files.class))
    private List<Files> filesList;

    @ArraySchema(schema = @Schema(description = "게시글 댓글 리스트", implementation = ResponseCommentDto.class))
    private List<ResponseCommentDto> commentList;

    @ArraySchema(schema = @Schema(description = "게시글 신고 id 리스트", implementation = Long.class))
    private List<Long> reportIdList;

    @Schema(description = "게시글 카테고리", example = "카테고리 이름")
    private String postCategory;

    @Enumerated(EnumType.STRING)
    @Schema(description = "게시글 종류", implementation = Type.class)
    private Type postType;

    @Schema(description = "게시글 제목", example = "제목입니다")
    private String postTitle;

    @Schema(description = "게시글 내용", example = "내용입니다")
    private String postContent;

    @Schema(description = "게시글 조회수", example = "0")
    private int postViews;

    @Schema(description = "게시글 좋아요수", example = "0")
    private int postLikeNo;

    @Schema(description = "게시글 생성 날짜", example = "2023-11-03T18:14:49.792+09:00")
    private LocalDateTime regDate;

    @Schema(description = "게시글 삭제 날짜", example = "2023-11-03T18:14:49.792+09:00")
    private LocalDateTime delDate;
}