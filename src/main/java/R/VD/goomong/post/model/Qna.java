package R.VD.goomong.post.model;

import R.VD.goomong.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Qna extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_qna_id")
    private Qna qna; // 부모 qna

    @OneToOne(mappedBy = "qna", cascade = CascadeType.ALL)
    private Qna children; // qna 답변

    @Column
    private String title; // 질문 제목

    @Column(nullable = false, length = 50000)
    private String Content; // 질문 및 답변 내용

    @Column
    private LocalDateTime delDate; // 삭제 날짜
}
