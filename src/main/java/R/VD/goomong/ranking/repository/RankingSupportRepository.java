package R.VD.goomong.ranking.repository;

import R.VD.goomong.member.model.Seller;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static R.VD.goomong.item.model.QItem.item;
import static R.VD.goomong.member.model.QSeller.seller;
import static R.VD.goomong.order.model.QOrder.order;
import static R.VD.goomong.review.model.QReview.review;

@Repository
@RequiredArgsConstructor
public class RankingSupportRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Seller> calculateSellerRanking() {
        return jpaQueryFactory
                .selectFrom(seller)
                .orderBy(seller.transactionCnt.desc(), seller.income.desc(), seller.reviewCnt.desc(), seller.rate.desc())
                .limit(5)
                .fetch();
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
