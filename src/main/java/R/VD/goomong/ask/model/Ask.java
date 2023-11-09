package R.VD.goomong.ask.model;

import R.VD.goomong.ask.dto.response.ResponseAnswerDto;
import R.VD.goomong.ask.dto.response.ResponseAskDto;
import R.VD.goomong.file.model.Files;
import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.report.model.Report;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Ask extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item; // 작성할 아이템

    @OneToMany
    @JoinColumn(name = "ask_id")
    @Builder.Default
    private List<Files> filesList = new ArrayList<>(); // 업로드 파일

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
                .regDate(this.getRegDate())
                .delDate(delDate)
                .build();
    }

    public ResponseAnswerDto toResponseAnswerDto() {

        return ResponseAnswerDto.builder()
                .id(id)
                .memberId(member.getMemberId())
                .filesList(filesList)
                .title(title)
                .content(content)
                .regDate(this.getRegDate())
                .delDate(delDate)
                .build();
    }
}
