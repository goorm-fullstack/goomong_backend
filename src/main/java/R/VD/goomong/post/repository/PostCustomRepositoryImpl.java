package R.VD.goomong.post.repository;

import R.VD.goomong.post.model.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static R.VD.goomong.post.model.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void addLikeCount(Post selectPost) {
        jpaQueryFactory.update(post)
                .set(post.postLikeNo, post.postLikeNo.add(1))
                .where(post.eq(selectPost))
                .execute();
    }

    @Override
    public void subLikeCount(Post selectPost) {
        jpaQueryFactory.update(post)
                .set(post.postLikeNo, post.postLikeNo.subtract(1))
                .where(post.eq(selectPost))
                .execute();
    }
}
