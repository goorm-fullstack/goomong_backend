package R.VD.goomong.post.model;

import R.VD.goomong.comment.dto.response.ResponseCommentDto;
import R.VD.goomong.comment.model.Comment;
import R.VD.goomong.file.model.Files;
import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.post.dto.response.ResponsePostDto;
import R.VD.goomong.report.model.Report;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 작성자

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>(); // 댓글

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Report> reportList = new ArrayList<>(); // 신고

    @OneToMany
    @JoinColumn(name = "post_id")
    @Builder.Default
    private List<Image> imageList = new ArrayList<>(); // 게시글 이미지

    @OneToMany
    @JoinColumn(name = "post_id")
    @Builder.Default
    private List<Files> fileList = new ArrayList<>(); // 게시글 파일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_category_id")
    private Category postCategory;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type postType; // 게시글 종류(ex. 커뮤니티/QnA 등등)

    @Column(nullable = false)
    private String postTitle; // 게시글 제목

    @Column(nullable = false, length = 50000)
    private String postContent; // 게시글 내용

    @Column(nullable = false)
    private int postViews; // 게시글 조회수

    @Column(nullable = false)
    private int postLikeNo; // 게시글 좋아요수

    @Column(nullable = false)
    @Builder.Default
    private Boolean isFix = false; // 고정 글 여부

    @Column
    private LocalDateTime delDate; // 삭제 날짜

    public ResponsePostDto toResponsePostDto() {

        List<ResponseCommentDto> comments = new ArrayList<>();
        for (Comment comment : commentList) {
            if (comment.getParentComment() == null && comment.getDelDate() == null) {
                comments.add(comment.toResponseCommentDto());
            }
        }

        List<Long> reports = new ArrayList<>();
        for (Report report : reportList) {
            if (report.getDelDate() == null) reports.add(report.getId());
        }

        return ResponsePostDto.builder()
                .id(id)
                .memberId(member.getMemberId())
                .memberAddress(member.getSaleSimpleAddress() != null ? member.getSaleSimpleAddress() : null)
                .memberImageList(member.getProfileImages() != null ? member.getProfileImages() : null)
                .postCategory(postCategory == null ? null : postCategory.getDelDate() == null ? postCategory.getCategoryName() : null)
                .postLikeNo(postLikeNo)
                .imageList(imageList)
                .postContent(postContent)
                .postViews(postViews)
                .postTitle(postTitle)
                .regDate(this.getRegDate())
                .reportIdList(reports)
                .postType(postType)
                .commentList(comments)
                .commentNo(commentList.size())
                .delDate(delDate)
                .filesList(fileList)
                .build();
    }
}
