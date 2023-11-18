package R.VD.goomong.support.dto.request;

import R.VD.goomong.support.model.EmailMemberSave;
import lombok.*;

@Data
@NoArgsConstructor
public class RequestEmailMemberSaveDTO {
    private String email;

    private String code;

    private Boolean emailChecked = false;

    @Builder(toBuilder = true)
    public RequestEmailMemberSaveDTO(String email, String code, Boolean emailChecked) {
        this.email = email;
        this.code = code;
        this.emailChecked = emailChecked;
    }

    public EmailMemberSave toEntity() {
        return EmailMemberSave.builder()
                .email(email)
                .code(code)
                .emailChecked(emailChecked)
                .build();
    }

}
