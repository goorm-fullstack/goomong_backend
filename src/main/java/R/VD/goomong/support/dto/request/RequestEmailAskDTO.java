package R.VD.goomong.support.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestEmailAskDTO {

    private String email;

    private String title;

    private String content;

}