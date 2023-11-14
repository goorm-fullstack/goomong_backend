package R.VD.goomong.review.repository;

import R.VD.goomong.review.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewCustomRepository {

    Page<Review> findAllByMemberId(Long memberId, Pageable pageable);
}
