package R.VD.goomong.point.repository;

import R.VD.goomong.point.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    List<Point> findByMember_Id(Long MemberId);

    @Query("SELECT SUM(p.amount) FROM Point p WHERE p.member.id = :memberId")
    Integer sumPointsByMember_Id(@Param("memberId") Long memberId);

}
