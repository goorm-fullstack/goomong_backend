package R.VD.goomong.member.repository;

import R.VD.goomong.member.model.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findByMemberId(String memberId);

    Page<Seller> findAllByMemberIdContainingIgnoreCaseOrNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String memberIdKeyword,
            String nameKeyword,
            String descriptionKeyword,
            Pageable pageable
    );


}
