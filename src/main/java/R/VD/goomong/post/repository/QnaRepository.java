package R.VD.goomong.post.repository;

import R.VD.goomong.post.model.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> {
}
