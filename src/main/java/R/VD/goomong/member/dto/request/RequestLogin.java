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


    @Builder
    public RequestLogin(String memberId, String memberPassword) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
    }

    public Member toEntity() {
        return Member.builder()
                .memberId(memberId)
                .memberPassword(memberPassword)
                .build();
    }

}