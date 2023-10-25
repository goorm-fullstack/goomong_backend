package R.VD.goomong.report.model;

import R.VD.goomong.member.model.Member;
import R.VD.goomong.post.model.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 작성자

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "report")
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 신고 대상 게시글

    @Column(nullable = false)
    private String reportTitle; // 신고 제목

    @Column(nullable = false, length = 50000)
    private String reportContent; // 신고 내용

    @Column(nullable = false)
    private LocalDateTime regDate; // 신고 날짜

    @Column
    private LocalDateTime chgDate; // 수정 날짜

    @Column
    private LocalDateTime delDate; // 삭제 날짜
}
