package R.VD.goomong.support.repository;

import R.VD.goomong.support.model.EmailMemberSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailMemberSaveRepository extends JpaRepository<EmailMemberSave, Long> {

    Optional<EmailMemberSave> findByMemberEmail(String memberEmail);

    void deleteByMemberEmail(String memberEmail);

}
