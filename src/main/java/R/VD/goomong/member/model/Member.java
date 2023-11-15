package R.VD.goomong.member.model;

import R.VD.goomong.ask.model.Ask;
import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.order.model.Order;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.review.model.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String memberId;

    @Column(nullable = false)
    private String memberPassword;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false)
    private String memberEmail;

    @Column(nullable = false)
    @JsonFormat(timezone = "Asia/Seoul")
    private LocalDateTime memberSignupTime;

    private LocalDateTime memberDeleteTime;

    @Column(nullable = false)
    private Long zipCode;                                    //우편 번호

    @Column(nullable = false)
    private String simpleAddress;                            //간단 주소

    @Column(nullable = false)
    private String detailAddress;                            //상세 주소

    @Column(nullable = false)
    private String memberRole;                                //권한

    @Column(nullable = false)
    private Long memberLoginFailed;                              //로그인 실패 횟수

    @OneToMany
    @Builder.Default
    private List<Item> itemList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Post> postList = new ArrayList<>();

    @OneToMany
    @JsonIgnore
    @Builder.Default
    private List<Order> orderList = new ArrayList<>();

    @OneToMany
    @Builder.Default
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany
    @Builder.Default
    private List<Ask> askList = new ArrayList<>();

    @Column
    private LocalDateTime delDate; // 삭제 날짜

    public void update(String memberId, String memberPassword, String memberName, String memberEmail, Long zipCode, String simpleAddress, String detailAddress) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.zipCode = zipCode;
        this.simpleAddress = simpleAddress;
        this.detailAddress = detailAddress;
    }

    //    public ResponseMemberDto toResponseMemberDto() {
//        List<ResponsePostDto> posts = null;
//        if (postList != null) posts = postList.stream().map(Post::toResponsePostDto).toList();
//
//        return ResponseMemberDto.builder()
//                .id(id)
//                .memberId(memberId)
//                .posts(posts)
//                .build();
//    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberId, itemList, postList, orderList, reviewList, askList);
    }
}