package R.VD.goomong.support.model;

import R.VD.goomong.file.model.Files;
import R.VD.goomong.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    @Column
    private String email;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    @Builder.Default
    private Boolean isEmailOpened = false;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "files_id")
    @Builder.Default
    private List<Files> filesList = new ArrayList<>();

    @Column
    private LocalDateTime delDate;

    @OneToOne(mappedBy = "emailAsk", cascade = CascadeType.ALL, orphanRemoval = true)
    private EmailAnswer emailAnswer;

}