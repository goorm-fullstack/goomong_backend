package R.VD.goomong.support.dto.response;

import R.VD.goomong.support.model.EmailAsk;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseEmailAskListDTO {

    private Long emailAskId;

    private String email;

    private String title;

    private Boolean isEmailOpened;

    private LocalDateTime regDate;

    public ResponseEmailAskListDTO(EmailAsk emailAsk) {
        this.emailAskId = emailAsk.getEmailAskId();
        this.title = emailAsk.getTitle();
        this.email = emailAsk.getEmail();
        this.isEmailOpened = emailAsk.getIsEmailOpened();
        this.regDate = emailAsk.getRegDate();
    }

}