package R.VD.goomong.ranking.repository;

import R.VD.goomong.order.model.Status;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static R.VD.goomong.item.model.QItem.item;
import static R.VD.goomong.item.model.QItemCategory.itemCategory;
import static R.VD.goomong.member.model.QMember.member;
import static R.VD.goomong.order.model.QOrder.order;
import static R.VD.goomong.review.model.QReview.review;

@Repository
@RequiredArgsConstructor
public class RankingSupportRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Tuple> calculateSellerRanking(String categoryTitle, String sortBy) {
        JPAQuery<Tuple> query = jpaQueryFactory
                .select(
                        member.id, member.memberName, member.profileImages,
                        itemCategory.title, item.countDistinct(), order.price.sum(),
                        review.rate.avg(), review.id.count()
                )
                .from(order)
                .join(order.orderItem, item)
                .join(item.member, member)
                .leftJoin(item.reviewList, review)
                .join(item.itemCategories, itemCategory)
                .where(order.status.eq(Status.COMPLETE))
                .groupBy(member.id);

        if (categoryTitle != null && !categoryTitle.trim().isEmpty()) {
            query.where(itemCategory.title.eq(categoryTitle));
        }

        switch (sortBy) {
            case "totalSales":
                query.orderBy(order.price.sum().desc());
                break;
            case "reviewCount":
                query.orderBy(review.id.count().desc());
                break;
            case "reviewAvg":
                query.orderBy(review.rate.avg().desc());
                break;
            case "transaction":
            default:
                query.orderBy(item.countDistinct().desc());
        }

        return query.limit(10).fetch();
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
                .where(order.status.eq(Status.COMPLETE)
                        .and(order.regDate.between(start, end)))
                .groupBy(item.member)
                .orderBy(item.count().desc())
                .limit(5)
                .fetch();
    }

    // 누적 거래 순
    public List<Tuple> calculateTop5SellersBySalesAmount(LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory
                .select(order.member, order.price.sum())
                .from(order)
                .where(order.status.eq(Status.COMPLETE)
                        .and(order.regDate.between(start, end)))
                .groupBy(order.member)
                .orderBy(order.price.sum().desc())
                .limit(5)
                .fetch();
    }

}
