package R.VD.goomong.statistics.repository;

import R.VD.goomong.order.model.Status;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static R.VD.goomong.order.model.QOrder.order;
import static R.VD.goomong.review.model.QReview.review;


@Repository
@RequiredArgsConstructor
public class StatisticsRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Transactional(readOnly = true)
    public Tuple getReviewStatistics() {
        return jpaQueryFactory
                .select(review.rate.avg(), review.id.countDistinct())
                .from(review)
                .fetchOne();
    }

    @Transactional(readOnly = true)
    public Tuple getOrderStatistics() {
        return jpaQueryFactory
                .select(order.id.countDistinct(), order.price.sum())
                .from(order)
                .where(order.status.eq(Status.COMPLETE))
                .fetchOne();
    }
}

