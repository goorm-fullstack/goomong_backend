package R.VD.goomong.image.model;

import R.VD.goomong.global.model.BaseDateEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Image extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//DB 인덱스 번호
    private String fileName;//파일명
    private String saveFileName;//저장한 파일명
    private String path;//저장된 경로
}
