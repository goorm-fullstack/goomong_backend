package R.VD.goomong.support.model;

import R.VD.goomong.global.model.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EmailMemberSave extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberEmail;

    private String code;

    private Boolean emailChecked;

    public void emailUpdate(String memberEmail, String code, Boolean emailChecked) {
        this.memberEmail = memberEmail;
        this.code = code;
        this.emailChecked = emailChecked;
    }

    public void emailCheckedSuccess() {
        this.emailChecked = true;
    }
}