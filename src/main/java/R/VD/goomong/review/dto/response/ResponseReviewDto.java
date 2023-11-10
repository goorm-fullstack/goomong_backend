package R.VD.goomong.review.dto.response;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.report.dto.response.ResponseReportDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponseReviewDto {
    private Long id;//DB 인덱스
    private String memberId;//작성자
    private List<Image> imageList; // 이미지
    private List<ResponseReportDto> reportList; // 신고
    private String title; // 제목
    private String content;//리뷰 내용
    private Float rate;//평점
    private LocalDateTime regDate; // 작성 날짜
    private LocalDateTime delDate; // 삭제 날짜
}
