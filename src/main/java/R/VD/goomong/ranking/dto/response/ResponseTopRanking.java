package R.VD.goomong.ranking.dto.response;

import R.VD.goomong.image.model.Image;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseTopRanking {
    private Long memberId;
    private String memberName;
    private List<Image> imageList;
    private String categoryTitle;
    private Long transaction;
    private Long totalSales;
    private Long reviewCount;
    private Double totalRating;
}