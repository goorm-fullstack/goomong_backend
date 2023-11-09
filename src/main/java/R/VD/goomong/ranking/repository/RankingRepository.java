package R.VD.goomong.ranking.repository;

import R.VD.goomong.ranking.model.Ranking;
import R.VD.goomong.ranking.model.RankingPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RankingRepository extends JpaRepository<Ranking, Long> {

    List<Ranking> findByPeriod(RankingPeriod period);

    void deleteAllByPeriodAndDelDateIsNull(RankingPeriod period);

}
