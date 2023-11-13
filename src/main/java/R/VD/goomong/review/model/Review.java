package R.VD.goomong.review.model;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.report.model.Report;
import R.VD.goomong.review.dto.response.ResponseReviewDto;
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
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//DB 인덱스

    @ManyToOne
    @JsonIgnore
    private Member member;//작성자

    @ManyToOne
    @JsonIgnore
    private Item item;//리뷰를 남길 아이템

    @OneToMany
    private List<Image> imageList = new ArrayList<>();

    private String content;//리뷰 내용
    private Float rate;//평점

    @Column
    private ZonedDateTime delDate;

    public ResponseReviewDto toResponseReviewDto() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.XXX");

        List<Long> reports = new ArrayList<>();
        for (Report report : reportList) {
            if (report.getDelDate() == null) reports.add(report.getId());
        }

        return ResponseReviewDto.builder()
                .id(id)
                .memberId(member.getMemberId())
                .imageList(imageList)
                .reportIdList(reports)
                .title(title)
                .content(content)
                .regDate(this.getRegDate().format(dateTimeFormatter))
                .delDate(delDate != null ? delDate.format(dateTimeFormatter) : null)
                .rate(rate)
                .build();
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }
}
