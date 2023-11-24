package R.VD.goomong.ranking.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ResponseTopRanking {
    private Long memberId;
    private String memberName;
    private String imagePath;
    private String saleSido;
    private Long transaction;
    private Long totalSales;
    private Long reviewCount;
    private Double totalRating;
}