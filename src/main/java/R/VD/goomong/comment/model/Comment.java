package R.VD.goomong.comment.model;

import R.VD.goomong.comment.dto.response.ResponseCommentDto;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.post.model.Post;
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
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 댓글이 달린 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment; // 부모 댓글, null인 경우 최상위 댓글

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<Comment> childrenComment = new ArrayList<>(); // 대댓글

    @Column(nullable = false, length = 50000)
    private String content; // 댓글 내용

    @Column(nullable = false)
    private int likeNo; // 댓글 좋아요 수

    @Column(nullable = false)
    private LocalDateTime regDate; // 댓글 작성 날짜

    @Column
    private LocalDateTime chgDate; // 댓글 수정 날짜

    @Column
    private LocalDateTime delDate; // 댓글 삭제 날짜

    public ResponseCommentDto toResponseCommentDto() {
        return ResponseCommentDto.builder()
                .id(id)
                .memberId(member.getMemberId())
                .content(content)
                .likeNo(likeNo)
                .regDate(regDate)
                .chgDate(chgDate)
                .delDate(delDate)
                .build();
    }
}
