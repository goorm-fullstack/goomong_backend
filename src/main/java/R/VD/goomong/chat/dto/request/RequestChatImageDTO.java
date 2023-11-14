package R.VD.goomong.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestChatImageDTO {

    private Long roomId;
    private Long memberId;
    private MultipartFile[] image;

}
