package R.VD.goomong.review.model;

import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.report.dto.response.ResponseReportDto;
import R.VD.goomong.report.model.Report;
import R.VD.goomong.review.dto.response.ResponseReviewDto;
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
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//DB 인덱스

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;//작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;//리뷰를 남길 아이템

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Report> reportList = new ArrayList<>(); // 신고

    @OneToMany
    @JoinColumn(name = "review_id")
    @Builder.Default
    private List<Image> imageList = new ArrayList<>();

    @Column(nullable = false)
    private String title; // 리뷰 제목

    @Column(nullable = false)
    private String content;//리뷰 내용

    @Column(nullable = false)
    private Float rate;//평점

    @Column
    private ZonedDateTime delDate;

    public ResponseReviewDto toResponseReviewDto() {

        List<ResponseReportDto> reports = new ArrayList<>();
        for (Report report : reportList) {
            if (report.getDelDate() == null) reports.add(report.toResponseReportDto());
        }

        return ResponseReviewDto.builder()
                .id(id)
                .memberId(member.getMemberId())
                .imageList(imageList)
                .reportList(reports)
                .title(title)
                .content(content)
                .regDate(this.getRegDate())
                .delDate(delDate)
                .rate(rate)
                .build();
    }
}