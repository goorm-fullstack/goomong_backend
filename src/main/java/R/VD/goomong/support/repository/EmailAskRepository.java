package R.VD.goomong.support.repository;

import R.VD.goomong.support.model.EmailAsk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailAskRepository extends JpaRepository<EmailAsk, Long> {

}
