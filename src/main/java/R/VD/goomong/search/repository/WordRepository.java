package R.VD.goomong.search.repository;

import R.VD.goomong.search.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {

    Optional<Word> findByKeyword(String Keyword);

    @Modifying
    @Query(value = "UPDATE Word SET word_count = word_count + 1 WHERE keyword = :keyword", nativeQuery = true)
    void incrementWordCount(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM Word w WHERE w.reg_date > :cutoff ORDER BY w.word_count DESC LIMIT 5", nativeQuery = true)
    List<Word> findTopWords(LocalDateTime cutoff);

}