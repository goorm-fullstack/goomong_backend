package R.VD.goomong.report.model;

import R.VD.goomong.ask.model.Ask;
import R.VD.goomong.comment.model.Comment;
import R.VD.goomong.file.model.Files;
import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.report.dto.response.ResponseReportDto;
import R.VD.goomong.review.model.Review;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 신고 대상 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment; // 신고 대상 댓글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review; // 신고 대상 리뷰글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ask_id")
    private Ask ask;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    @Builder.Default
    private List<Files> filesList = new ArrayList<>();

    @Column(nullable = false, length = 50000)
    private String reportReason; // 신고 이유

    @Column(nullable = false)
    private String reportCheck; // 신고 내용 확인 여부 (확인 안함 : 처리 중, 확인함 : 처리완료)

    @Column
    private String reportResult; // 신고 처리 결과 (삭제 처리, 이상 없음)

    @Column
    private ZonedDateTime delDate;

    public ResponseReportDto toResponseReportDto() {
        return ResponseReportDto.builder()
                .id(id)
                .memberId(member.getMemberId())
                .post(post != null ? post.toResponsePostDto() : null)
                .comment(comment != null ? comment.toResponseCommentDto() : null)
                .review(review != null ? review.toResponseReviewDto() : null)
                .ask(ask != null ? ask.toResponseAskDto() : null)
                .filesList(filesList)
                .reportReason(reportReason)
                .reportCheck(reportCheck)
                .reportResult(reportResult)
                .regDate(this.getRegDate())
                .delDate(delDate)
                .build();
    }
}
