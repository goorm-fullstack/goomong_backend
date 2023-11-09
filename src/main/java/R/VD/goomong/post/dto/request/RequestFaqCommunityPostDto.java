package R.VD.goomong.post.dto.request;

import R.VD.goomong.post.model.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@AllArgsConstructor
@ToString
@Schema(description = "FAQ, 커뮤니티 게시판 작성 정보")
public class RequestFaqCommunityPostDto {

    @Positive
    @Schema(description = "작성자 id", example = "1", required = true)
    private Long memberId;

    @NotBlank
    @Schema(description = "카테고리", example = "카테고리 이름")
    private String postCategory;

    @NotBlank
    @Schema(description = "게시글 종류(예: FAQ, 커뮤니티)", example = "커뮤니티")
    private String postType;

    @NotBlank
    @Schema(description = "게시글 제목", example = "제목입니다")
    private String postTitle;

    @NotBlank
    @Schema(description = "게시글 내용", example = "내용입니다")
    private String postContent;

    public Post toEntity() {
        return Post.builder()
                .postCategory(postCategory)
                .postType(postType)
                .postTitle(postTitle)
                .postContent(postContent)
                .postViews(0)
                .postLikeNo(0)
                .build();
    }
}
