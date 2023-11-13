package R.VD.goomong.support.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestEmailAnswerDTO {

    private Long emailAskId;

    private Long adminId;

    private String title;

    private String content;

}
