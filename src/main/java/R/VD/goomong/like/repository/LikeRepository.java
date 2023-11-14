package R.VD.goomong.like.repository;

import R.VD.goomong.comment.model.Comment;
import R.VD.goomong.like.model.Like;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberAndPost(Member member, Post post);

    Optional<Like> findByMemberAndComment(Member member, Comment comment);

    Optional<Like> findByMemberAndReview(Member member, Review review);
}
