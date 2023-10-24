package R.VD.goomong.member.model;

import R.VD.goomong.ask.model.Ask;
import R.VD.goomong.order.model.Order;
import R.VD.goomong.review.model.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
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

    @Setter
    private LocalDateTime memberDeleteTime;

    @OneToMany
    @JsonIgnore
    private List<Order> orderList = new ArrayList<>();

    @OneToMany
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany
    private List<Ask> askList = new ArrayList<>();

}
