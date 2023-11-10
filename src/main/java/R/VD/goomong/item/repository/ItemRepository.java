package R.VD.goomong.item.repository;

import R.VD.goomong.item.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByStatus(String status, Pageable pageable);

    Page<Item> findAllByDelDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

}
