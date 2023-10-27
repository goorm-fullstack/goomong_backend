package R.VD.goomong.image.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//DB 인덱스 번호

    @Column(nullable = false)
    private String fileName;//파일명

    @Column(nullable = false)
    private String saveFileName;//저장한 파일명

    @Column(nullable = false)
    private String path;//저장된 경로
}
