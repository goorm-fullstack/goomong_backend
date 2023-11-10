package R.VD.goomong.support.dto.response;

import R.VD.goomong.member.model.Member;
import R.VD.goomong.support.model.EmailAsk;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEmailAsk {

    private Long emailAskId;

    private Member member;

    private String email;

    private String title;

    private String content;

    private Boolean isOpened;

    private LocalDateTime regDate;

    // ToDo : 파일
    // private List<Files> filesList = new ArrayList<>();
    public ResponseEmailAsk(EmailAsk emailAsk) {
        this.emailAskId = emailAsk.getEmailAskId();
        this.member = emailAsk.getMember();
        this.email = emailAsk.getEmail();
        this.title = emailAsk.getTitle();
        this.content = emailAsk.getContent();
        this.isOpened = emailAsk.getIsEmailOpened();
        this.regDate = emailAsk.getRegDate();
    }

}
