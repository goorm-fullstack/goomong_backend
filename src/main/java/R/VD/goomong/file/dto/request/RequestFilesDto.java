package R.VD.goomong.file.dto.request;

import R.VD.goomong.file.model.Files;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestFilesDto {

    private String fileName;//파일명
    private String saveFileName;//저장한 파일명
    private String path;//저장된 경로

    public Files toEntity() {
        return Files.builder()
                .fileName(fileName)
                .saveFileName(saveFileName)
                .path(path)
                .build();
    }
}
