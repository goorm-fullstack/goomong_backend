package R.VD.goomong.search.repository;

import R.VD.goomong.search.model.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<Search, Long> {

    @Query(value = "SELECT w.keyword, COUNT(*) FROM search s " +
            "JOIN word w ON s.word_id = w.word_id " +
            "WHERE s.reg_date BETWEEN :startTime AND :endTime " +
            "GROUP BY w.keyword " +
            "ORDER BY COUNT(*) DESC " +
            "LIMIT 5",
            nativeQuery = true)
    List<Object[]> findTopKeywordsNative(LocalDateTime startTime, LocalDateTime endTime);

    @Query(value = "SELECT * FROM Search WHERE member_id = :memberId and del_date != NULL ORDER BY reg_date DESC LIMIT 3", nativeQuery = true)
    List<Search> findRecentSearchesByMemberId(@Param("memberId") Long memberId);

}