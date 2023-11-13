package R.VD.goomong.ask.model;

import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;//작성자

    @ManyToOne
    private Item item;//작성할 아이템

    @OneToMany
    private List<Ask> asks = new ArrayList<>();//답변 내용들

    
    @OneToMany(mappedBy = "ask", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Report> reportList = new ArrayList<>();

    @Column(nullable = false)
    private String title; // 문의 제목

    @Column(nullable = false, length = 50000)
    private String content; // 문의 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_ask_id")
    private Ask ask; //부모 문의

    @OneToMany(mappedBy = "ask", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Ask> answerList = new ArrayList<>(); // 답변 내용들

    @Column
    private ZonedDateTime delDate;

    public ResponseAskDto toResponseAskDto() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.XXX");

        List<ResponseAnswerDto> answers = new ArrayList<>();
        if (!answerList.isEmpty()) {
            for (Ask ask1 : answerList) {
                if (ask1.getDelDate() == null && ask1.getAsk() != null) answers.add(ask1.toResponseAnswerDto());
            }
        }

        List<Long> reports = new ArrayList<>();
        if (!reportList.isEmpty()) {
            for (Report report : reportList) {
                if (report.getDelDate() == null) reports.add(report.getId());
            }
        }

        return ResponseAskDto.builder()
                .id(id)
                .memberId(member.getMemberId())
                .item(new ResponseItemDto(item))
                .filesList(filesList)
                .reportListId(reports)
                .title(title)
                .content(content)
                .answerList(answers)
                .regDate(this.getRegDate().format(dateTimeFormatter))
                .delDate(delDate != null ? delDate.format(dateTimeFormatter) : null)
                .build();
    }

    public ResponseAnswerDto toResponseAnswerDto() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.XXX");

        return ResponseAnswerDto.builder()
                .id(id)
                .memberId(member.getMemberId())
                .filesList(filesList)
                .title(title)
                .content(content)
                .regDate(this.getRegDate().format(dateTimeFormatter))
                .delDate(delDate != null ? delDate.format(dateTimeFormatter) : null)
                .build();
    }
}
