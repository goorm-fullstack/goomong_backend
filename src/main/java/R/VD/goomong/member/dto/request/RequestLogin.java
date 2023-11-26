package R.VD.goomong.member.dto.request;

import R.VD.goomong.member.model.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestLogin {

    private String memberId;

    private String memberPassword;

    private Long memberLoginFailed;


    @Builder
    public RequestLogin(String memberId, String memberPassword, Long memberLoginFailed) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberLoginFailed = memberLoginFailed;
    }

    public Member toEntity() {
        return Member.builder()
                .memberId(memberId)
                .memberPassword(memberPassword)
                .build();
    }
}