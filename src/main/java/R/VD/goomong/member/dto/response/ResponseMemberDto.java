package R.VD.goomong.member.dto.response;

import R.VD.goomong.post.dto.response.ResponseItemPostDto;
import lombok.*;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponseMemberDto {

    private Long id;
    private String memberId;
    private List<ResponseItemPostDto> posts;
}
