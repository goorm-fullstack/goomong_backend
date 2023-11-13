package R.VD.goomong.post.dto.request;

import R.VD.goomong.post.model.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
@Schema(description = "판매/기부/교환 게시글 작성 정보")
public class RequestItemPostDto {

    @Positive
    @Schema(description = "작성자 id", example = "1", required = true)
    private Long memberId;

    @Positive
    @Schema(description = "판매/교환/기부 일 시 상품 id", example = "1", required = true)
    private Long itemId;

    @NotBlank(message = "게시글 종류를 입력해주세요.")
    @Schema(description = "게시글 종류(예: 판매, 기부, 교환)", example = "판매")
    private String postType; // 게시글 종류(ex. 커뮤니티/QnA 등등)

    @NotBlank(message = "게시글 제목을 입력해주세요.")
    @Schema(description = "게시글 제목", example = "제목입니다.")
    private String postTitle; // 게시글 제목

    @NotBlank(message = "게시글 내용을 입력해주세요.")
    @Schema(description = "게시글 내용", example = "내용입니다.")
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
