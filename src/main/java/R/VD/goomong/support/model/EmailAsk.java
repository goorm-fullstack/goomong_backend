package R.VD.goomong.support.model;

import R.VD.goomong.file.model.Files;
import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.member.model.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EmailAsk extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailAskId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private Boolean isEmailOpened = false;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "email_ask_id")
    @Builder.Default
    private List<Files> filesList = new ArrayList<>();

    @Column
    private ZonedDateTime delDate;
    
}