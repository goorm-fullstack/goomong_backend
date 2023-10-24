package R.VD.goomong.post.model;

import R.VD.goomong.comment.dto.response.ResponseCommentDto;
import R.VD.goomong.comment.model.Comment;
import R.VD.goomong.file.model.Files;
import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.post.dto.response.ResponsePostCategoryDto;
import R.VD.goomong.post.dto.response.ResponsePostDto;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item; // 상품

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postCategory_id")
    private PostCategory postCategory; // 카테고리

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>(); // 댓글

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private List<Image> imageList = new ArrayList<>(); // 게시글 이미지

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private List<Files> fileList = new ArrayList<>(); // 게시글 파일

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
    private LocalDateTime delDate; // 삭제일

    // response로 변환
    public ResponsePostDto toResponsePostDto() {

        ResponseItemDto item1 = null;
        if (item != null) item1 = new ResponseItemDto(item);
        ResponsePostCategoryDto postCategory1 = null;
        if (postCategory != null) postCategory1 = postCategory.toResponsePostCategoryDto();

        List<ResponseCommentDto> comments = new ArrayList<>();
        for (Comment comment : commentList) {
            if (comment.getParentComment() == null && comment.getDelDate() == null) {
                comments.add(comment.toResponseCommentDto());
            }
        }

        return ResponsePostDto.builder()
                .id(id)
                .member(member.getMemberId())
                .item(item1)
                .postCategory(postCategory1)
                .postType(postType)
                .postTitle(postTitle)
                .postContent(postContent)
                .postViews(postViews)
                .postLikeNo(postLikeNo)
                .imageList(imageList)
                .fileList(fileList)
                .commentList(comments)
                .regDate(this.getRegDate())
                .chgDate(this.getChgDate())
                .delDate(delDate)
                .build();
    }
}