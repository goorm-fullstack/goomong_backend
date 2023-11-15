package R.VD.goomong.review.repository;

import R.VD.goomong.review.model.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static R.VD.goomong.review.model.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void addLikeCount(Review selectReview) {
        jpaQueryFactory.update(review)
                .set(review.likeNo, review.likeNo.add(1))
                .where(review.eq(selectReview))
                .execute();
    }

    @Override
    public void subLikeCount(Review selectReview) {
        jpaQueryFactory.update(review)
                .set(review.likeNo, review.likeNo.subtract(1))
                .where(review.eq(selectReview))
                .execute();
    }
}
