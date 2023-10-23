package R.VD.goomong.ask.model;

import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.model.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Ask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;//작성자

    @ManyToOne
    private Item item;//작성할 아이템

    private String title;//문의 제목
    private String content;//문의 내용

    @ManyToOne
    private Ask ask;//부모 문의

    @OneToMany
    private List<Ask> asks = new ArrayList<>();//답변 내용들
}
