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
import jakarta.validation.constraints.NotNull;
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

    private Long buyZipCode;                                    //구매자 우편 번호

    private String buySimpleAddress;                            //구매자 간단 주소

    private String buyDetailAddress;                            //구매자 상세 주소

    private Long saleZipCode;                                    //판매자 우편 번호

    private String saleSimpleAddress;                            //판매자 간단 주소

    private String saleDetailAddress;                            //판매자 상세 주소

    @Column(nullable = false)
    private String memberRole;                                //권한

    @Column(nullable = false)
    private Long memberLoginFailed;                              //로그인 실패 횟수

    @Column(nullable = false)
    private Boolean isKakao;                                    //카카오 아이디인가?

    private String saleInfo;                                      //판매자 소개

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

    @Column
    private LocalDateTime delDate; // 삭제 날짜

    public void update(String memberId, String memberPassword, String memberName, String memberEmail, Long buyZipCode, String buySimpleAddress, String buyDetailAddress, Long saleZipCode, String saleSimpleAddress, String saleDetailAddress, String saleInfo) {
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