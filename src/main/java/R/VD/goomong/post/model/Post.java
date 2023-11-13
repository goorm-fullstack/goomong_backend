package R.VD.goomong.post.model;

import R.VD.goomong.comment.dto.response.ResponseCommentDto;
import R.VD.goomong.comment.model.Comment;
import R.VD.goomong.file.model.Files;
import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.post.dto.response.ResponseFaqCommunityPostDto;
import R.VD.goomong.post.dto.response.ResponseItemPostDto;
import R.VD.goomong.report.model.Report;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item; // 상품

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

    @Column
    private String postCategory;

    @Column(nullable = false)
    private String postType; // 게시글 종류(ex. 커뮤니티/QnA 등등)

    @Column(nullable = false)
    private String postTitle; // 게시글 제목

    @Column(nullable = false, length = 50000)
    private String postContent; // 게시글 내용

    @Column(nullable = false)
    private int postViews; // 게시글 조회수

    @Column(nullable = false)
    private int postLikeNo; // 게시글 좋아요수

    @Column
    private ZonedDateTime delDate; // 삭제 날짜

    // ResponseItemPostDto로 변환
    public ResponseItemPostDto toResponseItemPostDto() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.XXX");

        ResponseItemDto item1 = null;
        if (item != null) item1 = new ResponseItemDto(item);

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

        return ResponseItemPostDto.builder()
                .id(id)
                .memberId(member.getMemberId())
                .item(item1)
                .postType(postType)
                .postTitle(postTitle)
                .postContent(postContent)
                .postViews(postViews)
                .postLikeNo(postLikeNo)
                .imageList(imageList)
                .fileList(fileList)
                .commentList(comments)
                .reportIdList(reports)
                .regDate(this.getRegDate().format(dateTimeFormatter))
                .delDate(delDate != null ? delDate.format(dateTimeFormatter) : null)
                .build();
    }

    public ResponseFaqCommunityPostDto toResponseFaqCoummunityDto() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.XXX");

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

        return ResponseFaqCommunityPostDto.builder()
                .id(id)
                .memberId(member.getMemberId())
                .postCategory(postCategory)
                .postLikeNo(postLikeNo)
                .imageList(imageList)
                .postContent(postContent)
                .postViews(postViews)
                .postTitle(postTitle)
                .regDate(this.getRegDate().format(dateTimeFormatter))
                .reportIdList(reports)
                .postType(postType)
                .commentList(comments)
                .delDate(delDate != null ? delDate.format(dateTimeFormatter) : null)
                .filesList(fileList)
                .build();
    }
}
