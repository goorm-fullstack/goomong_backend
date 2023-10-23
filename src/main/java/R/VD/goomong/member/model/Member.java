package R.VD.goomong.member.model;

import R.VD.goomong.ask.model.Ask;
import R.VD.goomong.order.model.Order;
import R.VD.goomong.review.model.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    @JsonIgnore
    private List<Order> orderList = new ArrayList<>();

    @OneToMany
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany
    private List<Ask> askList = new ArrayList<>();
}
