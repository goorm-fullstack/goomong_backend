package R.VD.goomong.item.repository;

import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAllByDelDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Item> findAllByStatus(Status status, Pageable pageable);
}