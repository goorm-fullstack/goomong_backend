package R.VD.goomong.file.repository;

import R.VD.goomong.file.model.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilesRepository extends JpaRepository<Files, Long> {

    Optional<Files> findByFileName(String fileName);
}
