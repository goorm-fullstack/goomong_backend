package R.VD.goomong.ranking.repository;

import R.VD.goomong.ranking.model.RankingType;
import R.VD.goomong.ranking.model.TopSellerRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopSellerRankingRepository extends JpaRepository<TopSellerRanking, Long> {
    void deleteAllByRankingTypeAndDelDateIsNull(RankingType type);
    
}
