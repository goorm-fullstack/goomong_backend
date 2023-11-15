package R.VD.goomong.ranking.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseTopRanking {
    private Long memberId;
    private String memberName;
    private String categoryTitle;
    private Long itemCount;
    private Long totalSales;
    private Long reviewCount;
}