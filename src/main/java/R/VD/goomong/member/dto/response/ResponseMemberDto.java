package R.VD.goomong.member.dto.response;

import lombok.*;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponseMemberDto {

    private Long id;
    private String memberId;
    // todo: 동규님꼐 전달 -> post내용 바껴서 수정 필요
//    private List<ResponseItemPostDto> posts;
}
