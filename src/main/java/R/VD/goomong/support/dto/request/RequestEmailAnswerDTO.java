package R.VD.goomong.support.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestEmailAnswerDTO {

    private Long emailAskId;

    private Long adminId;

    private String title;

    private String content;

    private MultipartFile[] files;
}
