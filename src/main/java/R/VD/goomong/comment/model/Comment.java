package R.VD.goomong.comment.model;

import R.VD.goomong.comment.dto.response.ResponseCommentDto;
import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.report.model.Report;
import R.VD.goomong.review.model.Review;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 댓글이 달린 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment; // 부모 댓글, null인 경우 최상위 댓글

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentComment", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> childrenComment = new ArrayList<>(); // 대댓글

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Report> reportList = new ArrayList<>(); // 신고

    @Column(nullable = false, length = 50000)
    private String content; // 댓글 내용

    @Column(nullable = false)
    private int likeNo; // 댓글 좋아요 수

    private LocalDateTime delDate;

    public ResponseCommentDto toResponseCommentDto() {

        List<ResponseCommentDto> list = new ArrayList<>();
        if (!childrenComment.isEmpty()) {
            for (Comment comment : childrenComment) {
                if (comment.getDelDate() == null) list.add(comment.toResponseCommentDto());
            }
        }

        List<Long> reports = new ArrayList<>();
        if (!reportList.isEmpty()) {
            for (Report report : reportList) {
                if (report.getDelDate() == null) reports.add(report.getId());
            }
        }

        return ResponseCommentDto.builder()
                .id(id)
                .memberId(member.getMemberId())
                .postId(post.getId())
                .postTitle(post.getPostTitle())
                .memberImageList(member.getProfileImages() != null ? member.getProfileImages() : null)
                .content(content)
                .likeNo(likeNo)
                .parentId(parentComment != null ? parentComment.getId() : null)
                .childrenComment(list)
                .reportIdList(reports)
                .regDate(this.getRegDate())
                .delDate(delDate)
                .build();
    }
}
