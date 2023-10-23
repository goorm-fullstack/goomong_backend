package R.VD.goomong.member.model;

import R.VD.goomong.item.model.Item;
import R.VD.goomong.post.model.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany
    private List<Item> itemList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Post> postList = new ArrayList<>();
}
