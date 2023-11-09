package R.VD.goomong.post.dto.request;

import R.VD.goomong.post.model.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class RequestPostDto {

    @Positive
    private Long memberId;

    private Long itemId;

    private Long postCategoryId;

    @NotBlank(message = "게시글 종류를 입력해주세요.")
    private String postType; // 게시글 종류(ex. 커뮤니티/QnA 등등)

    @NotBlank(message = "게시글 제목을 입력해주세요.")
    private String postTitle; // 게시글 제목

    @NotBlank(message = "게시글 내용을 입력해주세요.")
    private String postContent; // 게시글 내용

    // RequestPostDto 엔티티화
    public Post toEntity() {
        return Post.builder()
                .postType(postType)
                .postTitle(postTitle)
                .postContent(postContent)
                .postLikeNo(0)
                .postViews(0)
                .build();
    }
}
