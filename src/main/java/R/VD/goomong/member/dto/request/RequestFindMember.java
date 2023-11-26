package R.VD.goomong.member.dto.request;

import R.VD.goomong.member.model.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestFindMember {

    String memberName;
    String memberEmail;

    @Builder
    public RequestFindMember(String memberName, String memberEmail) {
        this.memberName = memberName;
        this.memberEmail = memberEmail;
    }

    public Member toEntity() {
        return Member.builder()
                .memberName(memberName)
                .memberEmail(memberEmail)
                .build();
    }
}
