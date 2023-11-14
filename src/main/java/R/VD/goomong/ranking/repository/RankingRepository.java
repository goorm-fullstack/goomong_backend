package R.VD.goomong.ranking.repository;

import R.VD.goomong.ranking.model.Ranking;
import R.VD.goomong.ranking.model.RankingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
    void deleteAllByRankingTypeAndDelDateIsNull(RankingType type);

}
