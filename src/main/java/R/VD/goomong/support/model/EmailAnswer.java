package R.VD.goomong.support.model;

import R.VD.goomong.file.model.Files;
import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.member.model.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EmailAnswer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailAnswerId;

    private String title;

    private String content;

    @OneToOne
    @JoinColumn(name = "email_ask_id")
    private EmailAsk emailAsk;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member admin;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "files_id")
    @Builder.Default
    private List<Files> filesList = new ArrayList<>();

}
