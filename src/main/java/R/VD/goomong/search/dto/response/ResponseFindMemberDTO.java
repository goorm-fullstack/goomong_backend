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
    private String categoryTitle;
    private Long itemCount;
    private Long totalSales;
    private Long reviewCount;
}