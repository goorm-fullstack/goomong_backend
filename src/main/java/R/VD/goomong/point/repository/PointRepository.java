package R.VD.goomong.point.repository;

import R.VD.goomong.point.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    List<Point> findByMember_Id(Long MemberId);

    // 총합 포인트 계산
    @Query("SELECT SUM(p.amount) FROM Point p WHERE p.member.id = :memberId")
    Integer sumPointsByMember_Id(@Param("memberId") Long memberId);

    // 만료 예정 포인트 계산
    @Query("SELECT SUM(p.amount) FROM Point p WHERE p.member.id = :memberId AND p.regDate >= :oneMonthOneWeekAgo AND p.regDate < :oneMonthAgo")
    Integer findExpiringPointsForMember(@Param("memberId") Long memberId, @Param("oneMonthOneWeekAgo") LocalDateTime oneMonthOneWeekAgo, @Param("oneMonthAgo") LocalDateTime oneMonthAgo);

    // 만료 포인트 찾기
    @Query("SELECT p.member, SUM(p.amount) FROM Point p WHERE p.regDate >= :startDateTime AND p.regDate < :endDateTime GROUP BY p.member")
    List<Object[]> sumExpiredPointsByMember(@Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);

}
