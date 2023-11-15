package R.VD.goomong.image.dto.request;

import R.VD.goomong.image.model.Image;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestImageDto {

    private String fileName;//파일명
    private String saveFileName;//저장한 파일명
    private String path;//저장된 경로

    public Image toEntity() {
        return Image.builder()
                .fileName(fileName)
                .saveFileName(saveFileName)
                .path(path)
                .build();
    }
}
