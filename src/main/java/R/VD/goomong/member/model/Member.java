package R.VD.goomong.member.model;

import R.VD.goomong.ask.model.Ask;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.dto.response.ResponseMemberDto;
import R.VD.goomong.order.model.Order;
import R.VD.goomong.post.dto.response.ResponsePostDto;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.review.model.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    private String memberId;

    private String name;

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

    private LocalDateTime delDate;

    public ResponseMemberDto toResponseMemberDto() {
        List<ResponsePostDto> posts = null;
        if (postList != null) posts = postList.stream().map(Post::toResponsePostDto).toList();

        return ResponseMemberDto.builder()
                .id(id)
                .memberId(memberId)
                .posts(posts)
                .build();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberId, itemList, postList, orderList, reviewList, askList, delDate);
    }
}
