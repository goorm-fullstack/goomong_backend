package R.VD.goomong.comment.repository;

import R.VD.goomong.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {

    Page<Comment> getAllByMemberId(Long memberId, Pageable pageable);

}