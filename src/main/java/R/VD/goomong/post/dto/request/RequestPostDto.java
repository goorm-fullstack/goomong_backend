package R.VD.goomong.post.dto.request;

import R.VD.goomong.post.model.Post;
import R.VD.goomong.post.model.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@AllArgsConstructor
@ToString
@Schema(description = "FAQ, 커뮤니티 게시판 작성 정보")
public class RequestPostDto {

    @Positive
    @Schema(description = "작성자 id", example = "1", required = true)
    private Long memberId;

    @Schema(description = "카테고리 id", example = "1")
    private Long postCategoryId;

    @Enumerated(EnumType.STRING)
    @Schema(description = "게시글 종류(예: FAQ, 커뮤니티)", implementation = Type.class, required = true)
    private Type postType;

    @NotBlank
    @Schema(description = "게시글 제목", example = "제목입니다")
    private String postTitle;

    @NotBlank
    @Schema(description = "게시글 내용", example = "내용입니다")
    private String postContent;

    public Post toEntity() {
        return Post.builder()
                .postType(postType)
                .postTitle(postTitle)
                .postContent(postContent)
                .postViews(0)
                .postLikeNo(0)
                .build();
    }
}
