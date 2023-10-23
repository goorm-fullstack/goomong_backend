package R.VD.goomong.order.model;

import R.VD.goomong.global.model.Address;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Item> item = new ArrayList<>();

    @ManyToOne
    private Member member;

    private int price;

    @OneToOne
    private Address address;

    @Enumerated(EnumType.STRING)
    private Status status;//배송 상태
}
