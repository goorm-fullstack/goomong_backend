package R.VD.goomong.member.model;

import R.VD.goomong.ask.model.Ask;
import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.dto.response.ResponseLogin;
import R.VD.goomong.like.model.Like;
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
@Builder(toBuilder = true)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String memberId;

    @Column(nullable = false)
    private String memberPassword;

    @Column(nullable = false)
    private String memberEmail;

    private String memberName;

    @Column(nullable = false)
    @JsonFormat(timezone = "Asia/Seoul")
    private LocalDateTime memberSignupTime;

    private LocalDateTime memberDeleteTime;

    private Long buyZipCode;                                    //구매자 우편 번호

    private String buySimpleAddress;                            //구매자 간단 주소

    private String buyDetailAddress;                            //구매자 상세 주소

    private Long saleZipCode;                                    //판매자 우편 번호

    private String saleSimpleAddress;                            //판매자 간단 주소

    private String saleDetailAddress;                            //판매자 상세 주소

    @Column(nullable = false)
    private String memberRole;                                   //권한

    @Column(nullable = false)
    private Long memberLoginFailed;                              //로그인 실패 횟수

    @Column(nullable = false)
    private Boolean isKakao;                                    //카카오 아이디인가?

    private String saleInfo;                                      //판매자 소개

    @OneToMany
    @JoinColumn(name = "image_id")
    private List<Image> profileImages = new ArrayList<>();        //프로필 이미지

    @OneToMany
    private List<Item> itemList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> postList = new ArrayList<>();

    @OneToMany
    @JsonIgnore
    private List<Order> orderList = new ArrayList<>();

    @OneToMany
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany
    private List<Ask> askList = new ArrayList<>();

    // responseMember 정보에 추가 부탁드립니다. ResponseLikeDto로 추가하시면 될 것 같습니다. (진환)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Like> likeList = new ArrayList<>();

    @Column
    private LocalDateTime delDate; // 삭제 날짜

    private Boolean emailChecked;       //이메일 인증 확인 여부

    public void memberUpdate(String memberId, String memberPassword, String memberName, String memberEmail, Long buyZipCode, String buySimpleAddress, String buyDetailAddress, Long saleZipCode, String saleSimpleAddress, String saleDetailAddress, String saleInfo) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.buyZipCode = buyZipCode;
        this.buySimpleAddress = buySimpleAddress;
        this.buyDetailAddress = buyDetailAddress;
        this.saleZipCode = saleZipCode;
        this.saleSimpleAddress = saleSimpleAddress;
        this.saleDetailAddress = saleDetailAddress;
        this.saleInfo = saleInfo;
    }

    public void changePassword(String memberId, String memberPassword) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
    }

    public void changeEmail(String memberId, String memberEmail) {
        this.memberId = memberId;
        this.memberPassword = memberEmail;
    }

    public void changeProfileImage(String memberId, List<Image> profileImages) {
        this.memberId = memberId;
        this.profileImages = profileImages;
    }

    public void emailCheckedSuccess() {
        this.emailChecked = true;               //이메일 인증 확인
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

    public void incrementLoginFailed() {
        if (memberLoginFailed == null) {
            memberLoginFailed = 1L;
        } else {
            memberLoginFailed++;
        }
    }

    public void resetLoginFailed() {
        memberLoginFailed = 0L;
    }

    public ResponseLogin toResponseLoginDto() {
        return ResponseLogin.builder()
                .id(id)
                .memberId(memberId)
                .memberPassword(memberPassword)
                .build();
    }
}