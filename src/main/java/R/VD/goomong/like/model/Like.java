package R.VD.goomong.like.model;

import R.VD.goomong.comment.model.Comment;
import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.like.dto.response.ResponseLikeDto;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.review.model.Review;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "like_table")
public class Like extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public ResponseLikeDto toResponseLikeDto() {
        return ResponseLikeDto.builder()
                .id(id)
                .postId(post != null ? post.getId() : null)
                .reviewId(review != null ? review.getId() : null)
                .commentId(comment != null ? comment.getId() : null)
                .build();
    }
}
