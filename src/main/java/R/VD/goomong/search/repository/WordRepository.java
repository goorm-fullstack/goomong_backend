package R.VD.goomong.search.repository;

import R.VD.goomong.search.Model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {

    Optional<Word> findByKeyword(String Keyword);

    @Modifying
    @Query(value = "UPDATE Word SET word_count = word_count + 1 WHERE keyword = :keyword", nativeQuery = true)
    void incrementWordCount(@Param("keyword") String keyword);

}
