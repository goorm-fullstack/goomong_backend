package R.VD.goomong.post.dto.request;

import R.VD.goomong.post.model.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class RequestPostDto {

    @NotBlank
    private Long memberId;

    private Long itemId;

    private Long postCategoryId;

    @NotBlank
    private String postType; // 게시글 종류(ex. 판매/기부/교환/커뮤니티 등등)

    @NotBlank
    private String postName; // 게시글 제목

    @NotBlank
    private String postContent; // 게시글 내용

    // RequestPostDto 엔티티화
    public Post toEntity() {
        return Post.builder()
                .postType(postType)
                .postName(postName)
                .postContent(postContent)
                .postLikeNo(0)
                .postViews(0)
                .regDate(LocalDateTime.now())
                .build();
    }
}
