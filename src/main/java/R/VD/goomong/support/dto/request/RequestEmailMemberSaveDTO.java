package R.VD.goomong.support.dto.request;

import R.VD.goomong.support.model.EmailMemberSave;
import lombok.*;

@Data
@NoArgsConstructor
public class RequestEmailMemberSaveDTO {
    private String memberEmail;

    private String code;

    private Boolean emailChecked = false;

    @Builder(toBuilder = true)
    public RequestEmailMemberSaveDTO(String memberEmail, String code, Boolean emailChecked) {
        this.memberEmail = memberEmail;
        this.code = code;
        this.emailChecked = emailChecked;
    }

    public EmailMemberSave toEntity() {
        return EmailMemberSave.builder()
                .memberEmail(memberEmail)
                .code(code)
                .emailChecked(emailChecked)
                .build();
    }

}
