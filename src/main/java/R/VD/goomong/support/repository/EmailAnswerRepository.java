package R.VD.goomong.support.repository;

import R.VD.goomong.support.model.EmailAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailAnswerRepository extends JpaRepository<EmailAnswer, Long> {

}
