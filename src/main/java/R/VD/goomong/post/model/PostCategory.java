package R.VD.goomong.post.model;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.post.dto.response.ResponsePostCategoryDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image; // 카테고리 썸네일(커뮤니티 카테고리의 경우)

    @Column(nullable = false)
    private String postCategoryName; // 카테고리 이름

    @Column(nullable = false)
    private String postCategoryGroup; // 카테고리 그룹(커뮤니티, FAQ)

    @Column(nullable = false)
    private LocalDateTime regDate; // 생성일

    @Column
    private LocalDateTime chgDate; // 수정일

    @Column
    private LocalDateTime delDate; // 삭제일

    // response로 변환
    public ResponsePostCategoryDto toResponsePostCategoryDto() {
        return ResponsePostCategoryDto.builder()
                .id(id)
                .postCategoryName(postCategoryName)
                .regDate(regDate)
                .chgDate(chgDate)
                .delDate(delDate)
                .build();
    }
}
