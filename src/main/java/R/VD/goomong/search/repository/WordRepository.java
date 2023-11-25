package R.VD.goomong.search.repository;

import R.VD.goomong.search.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {

    @Query(value = "SELECT * FROM Word w WHERE w.reg_date > :cutoff ORDER BY w.word_count DESC LIMIT 5", nativeQuery = true)
    List<Word> findTopWords(LocalDateTime cutoff);

}