package R.VD.goomong.post.model;

import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.post.dto.response.ResponseCategoryDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image; // 카테고리 썸네일(커뮤니티 카테고리의 경우)

    @Column(nullable = false)
    private String categoryName; // 카테고리 이름

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type categoryGroup; // 카테고리 그룹(커뮤니티, FAQ)

    @Column
    private LocalDateTime delDate;

    // response로 변환
    public ResponseCategoryDto toResponseCategoryDto() {

        return ResponseCategoryDto.builder()
                .id(id)
                .imagePath(image.getPath())
                .categoryName(categoryName)
                .regDate(this.getRegDate())
                .delDate(delDate)
                .build();
    }
}