package R.VD.goomong.ranking.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static R.VD.goomong.item.model.QItem.item;
import static R.VD.goomong.member.model.QMember.member;
import static R.VD.goomong.order.model.QOrder.order;
import static R.VD.goomong.review.model.QReview.review;

@Repository
@RequiredArgsConstructor
public class RankingSupportRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Tuple> calculateSellerRanking() {
        JPAQuery<Tuple> query = jpaQueryFactory
                .select(
                        item.member.id,
                        item.member.memberName,
                        item.member.saleSido, // 판매자 시/도
                        item.count(),
                        order.price.sum(),
                        review.rate.avg(),
                        review.id.count()
                )
                .from(order)
                .join(order.orderItem, item)
                .join(item.member, member)
                .leftJoin(item.reviewList, review)
                .groupBy(item.member.id)
                .orderBy(order.price.sum().multiply(0.5)
                        .add(item.count().multiply(0.2))
                        .add(review.rate.avg().multiply(0.2))
                        .add(review.id.count().multiply(0.1)).asc())
                .limit(5);

        return query.fetch();
    }

    // 리뷰 순
    public List<Tuple> calculateTop5SellersByReviewCount(LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory
                .select(item.member, review.id.count())
                .from(review)
                .join(review.item, item)
                .where(review.regDate.between(start, end))
                .groupBy(item.member)
                .orderBy(review.id.count().desc())
                .limit(5)
                .fetch();
    }

    // 재능 거래 순
    public List<Tuple> calculateTop5SellersByOrderCount(LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory
                .select(item.member, item.count())
                .from(order)
                .join(order.orderItem, item)
                .where(order.regDate.between(start, end))
                .groupBy(item.member)
                .orderBy(item.count().desc())
                .limit(5)
                .fetch();
    }

    // 누적 거래 순
    public List<Tuple> calculateTop5SellersBySalesAmount(LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory
                .select(item.member, order.price.sum())
                .from(order)
                .join(order.orderItem, item)
                .where(order.regDate.between(start, end))
                .groupBy(item.member)
                .orderBy(order.price.sum().desc())
                .limit(5)
                .fetch();
    }

}
