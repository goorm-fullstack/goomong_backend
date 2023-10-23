package R.VD.goomong.post.model;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.model.Member;
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
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Column(nullable = false)
    private Member member; // 작성자

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item; // 상품

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postCategory_id")
    private PostCategory postCategory; // 카테고리

    @OneToMany
    @JoinColumn(name = "image_id")
    private List<Image> imageList = new ArrayList<>(); // 게시글 이미지

    @OneToMany
    @JoinColumn(name = "image_id")
    private List<Image> fileList = new ArrayList<>(); // 게시글 파일

    @Column(nullable = false)
    private String postType; // 게시글 종류(ex. 판매/기부/교환/커뮤니티 등등)

    @Column(nullable = false)
    private String postName; // 게시글 제목

    @Column(nullable = false, length = 50000)
    private String postContent; // 게시글 내용

    @Column(nullable = false)
    private int postViews; // 게시글 조회수

    @Column(nullable = false)
    private int postLikeNo; // 게시글 좋아요수

    @Column(nullable = false)
    private LocalDateTime regDate; // 생성일

    @Column
    private LocalDateTime chgDate; // 수정일

    @Column
    private LocalDateTime delDate; // 삭제일

    // response로 변환
    public ResponsePostDto toResponsePostDto() {
        return ResponsePostDto.builder()
                .id(id)
                .member(member.getMemberId())
                .item(new ResponseItemDto(item))
                .postType(postType)
                .postName(postName)
                .postContent(postContent)
                .postViews(postViews)
                .postLikeNo(postLikeNo)
                .regDate(regDate)
                .build();
    }
}
