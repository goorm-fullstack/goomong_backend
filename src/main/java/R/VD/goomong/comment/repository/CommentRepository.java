package R.VD.goomong.comment.repository;

import R.VD.goomong.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Modifying
    @Query("UPDATE Comment c SET c.likeNo = c.likeNo + 1 WHERE c.id = :commentId")
    void increaseLikeCount(@Param("commentId") Long commentId);

    Page<Comment> getAllByMemberId(Long memberId, Pageable pageable);
}
