package R.VD.goomong.item.repository;

import R.VD.goomong.item.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAllByDelDateBetween(ZonedDateTime start, ZonedDateTime end, Pageable pageable);

}
