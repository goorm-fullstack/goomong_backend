package R.VD.goomong.review.dto.response;

import R.VD.goomong.image.model.Image;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "리뷰 조회 정보")
public class ResponseReviewDto {

    @Schema(description = "리뷰 id", example = "1")
    private Long id;//DB 인덱스

    @Schema(description = "작성자 아이디", example = "아이디")
    private String memberId;//작성자

    @ArraySchema(schema = @Schema(description = "업로드한 이미지 리스트", implementation = Image.class))
    private List<Image> imageList; // 이미지

    @ArraySchema(schema = @Schema(description = "신고 id 리스트", implementation = Long.class))
    private List<Long> reportIdList; // 신고

    @Schema(description = "리뷰 제목", example = "제목입니다")
    private String title; // 제목

    @Schema(description = "리뷰 내용", example = "내용입니다")
    private String content;//리뷰 내용

    @Schema(description = "리뷰 평점", example = "5.0")
    private Float rate;//평점

    @Schema(description = "좋아요 수", example = "0")
    private int likeNo; // 좋아요

    @Schema(description = "리뷰 생성 날짜", example = "2023-11-03T18:14:49.792+09:00")
    private LocalDateTime regDate; // 작성 날짜

    @Schema(description = "리뷰 삭제 날짜", example = "2023-11-03T18:14:49.792+09:00")
    private LocalDateTime delDate; // 삭제 날짜
}
