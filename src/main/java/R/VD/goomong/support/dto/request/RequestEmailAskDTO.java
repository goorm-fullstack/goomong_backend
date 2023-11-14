package R.VD.goomong.support.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestEmailAskDTO {

    private String email;

    private String title;

    private String content;

    private MultipartFile[] files;

}