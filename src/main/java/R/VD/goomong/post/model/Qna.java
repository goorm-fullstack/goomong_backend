package R.VD.goomong.post.model;

import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.post.dto.response.ResponseAnswerForQuestionDto;
import R.VD.goomong.post.dto.response.ResponseQuestionDto;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_category_id")
    private Category category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_qna_id")
    private Qna qna; // 부모 qna

    @OneToOne(mappedBy = "qna", cascade = CascadeType.ALL)
    private Qna children; // qna 답변

    @Column
    private String title; // 질문 제목

    @Column(nullable = false, length = 50000)
    private String content; // 질문 및 답변 내용

    @Column
    private LocalDateTime delDate; // 삭제 날짜

    // response화(질문)
    public ResponseQuestionDto toResponseQuestionDto() {
        return ResponseQuestionDto.builder()
                .id(id)
                .categoryName(category.getCategoryName())
                .children(children != null ? children.toResponseAnswerForQuestionDto() : null)
                .title(title)
                .content(content)
                .regDate(this.getRegDate())
                .delDate(delDate)
                .build();
    }

    // response화(답변)
    public ResponseAnswerForQuestionDto toResponseAnswerForQuestionDto() {
        return ResponseAnswerForQuestionDto.builder()
                .id(id)
                .content(content)
                .regDate(this.getRegDate())
                .delDate(delDate)
                .build();
    }
}
