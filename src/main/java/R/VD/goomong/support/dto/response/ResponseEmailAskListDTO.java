package R.VD.goomong.support.dto.response;

import R.VD.goomong.support.model.EmailAsk;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseEmailAskListDTO {

    private Long emailAskId;

    private String memberName;

    private String title;

    private Boolean isEmailOpened;

    private LocalDateTime regDate;

    public ResponseEmailAskListDTO(EmailAsk emailAsk) {
        this.emailAskId = emailAsk.getEmailAskId();
        this.title = emailAsk.getTitle();
        if (emailAsk.getMember() == null)
            this.memberName = "비회원";
        else
            this.memberName = emailAsk.getMember().getMemberName();
        this.isEmailOpened = emailAsk.getIsEmailOpened();
        this.regDate = emailAsk.getRegDate();
    }

}