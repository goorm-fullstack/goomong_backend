package R.VD.goomong.review.model;

import R.VD.goomong.comment.dto.response.ResponseCommentDto;
import R.VD.goomong.comment.model.Comment;
import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.report.model.Report;
import R.VD.goomong.review.dto.response.ResponseReviewDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//DB 인덱스

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;//작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;//리뷰를 남길 아이템

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Report> reportList = new ArrayList<>(); // 신고

    @OneToMany
    @JoinColumn(name = "review_id")
    @Builder.Default
    private List<Image> imageList = new ArrayList<>();

    @Column(nullable = false)
    private String title; // 리뷰 제목

    @Column(nullable = false)
    private String content;//리뷰 내용

    @Column(nullable = false)
    private Float rate;//평점

    @Column(nullable = false)
    private int likeNo; // 좋아요

    @Column
    private LocalDateTime delDate;

    public ResponseReviewDto toResponseReviewDto() {

        List<Long> reports = new ArrayList<>();
        if (!reportList.isEmpty()) {
            for (Report report : reportList) {
                if (report.getDelDate() == null) reports.add(report.getId());
            }
        }

        List<ResponseCommentDto> comments = new ArrayList<>();
        if (!commentList.isEmpty()) {
            for (Comment comment : commentList) {
                if (comment.getParentComment() == null) comments.add(comment.toResponseCommentDto());
            }
        }

        return ResponseReviewDto.builder()
                .id(id)
                .memberId(member.getMemberId())
                .itemId(item.getId())
                .itemCategory(item.getItemCategories().size() != 0 ? item.getItemCategories().get(0).getTitle() : null)
                .itemName(item.getTitle())
                .imageList(imageList)
                .reportIdList(reports)
                .commentNo(commentList.size())
                .commentList(comments)
                .title(title)
                .content(content)
                .regDate(this.getRegDate())
                .delDate(delDate)
                .rate(rate)
                .likeNo(likeNo)
                .build();
    }
}