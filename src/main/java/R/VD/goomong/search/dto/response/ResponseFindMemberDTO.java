package R.VD.goomong.search.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseFindMemberDTO {
    private Long memberId;
    private String memberName;
    private String imagePath;
    private String saleSido;
    private String saleInfo;
    private Long transaction;
    private Long totalSales;
    private Long reviewCount;
    private Double totalRating;
}