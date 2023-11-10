package R.VD.goomong.post.repository;

import R.VD.goomong.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {

    @Modifying
    @Query("UPDATE Post p SET p.postViews = p.postViews + 1 WHERE p.id = :postId")
    void increaseViewCount(@Param("postId") Long postId);
}
