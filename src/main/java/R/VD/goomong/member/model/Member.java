package goomong.member.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import goomong.ask.model.Ask;
import goomong.order.model.Order;
import goomong.review.model.Review;
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
