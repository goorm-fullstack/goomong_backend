package R.VD.goomong.member.dto.request;

import R.VD.goomong.member.model.Member;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestMemberDto {

    private String memberId;

    public Member toEntity() {
        return Member.builder()
                .memberId(memberId)
                .build();
    }
}
