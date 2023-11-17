package R.VD.goomong.support.dto.response;

import R.VD.goomong.support.model.EmailMemberSave;
import lombok.Data;


@Data
public class ResponseEmailMemberSave {
    private String email;

    private String code;

    private Boolean emailChecked;

    public ResponseEmailMemberSave(EmailMemberSave emailMemberSave) {
        this.email = emailMemberSave.getEmail();
        this.code = emailMemberSave.getCode();
        this.emailChecked = emailMemberSave.getEmailChecked();
    }
}
