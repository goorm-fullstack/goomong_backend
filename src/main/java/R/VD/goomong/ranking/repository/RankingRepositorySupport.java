package R.VD.goomong.ranking.repository;

import R.VD.goomong.order.model.Status;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

import static R.VD.goomong.item.model.QItem.item;
import static R.VD.goomong.item.model.QItemCategory.itemCategory;
import static R.VD.goomong.order.model.QOrder.order;

@Repository
@RequiredArgsConstructor
public class RankingRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Tuple> calculateSellerSalesCount(ZonedDateTime start, ZonedDateTime end) {
        return jpaQueryFactory
                .select(item.member, itemCategory, item.countDistinct())
                .from(order)
                .join(order.orderItem, item)
                .join(item.itemCategories, itemCategory)
                .where(order.status.eq(Status.COMPLETE)
                        .and(order.regDate.between(start, end)))
                .groupBy(itemCategory, item.member)
                .orderBy(itemCategory.title.asc(), item.countDistinct().desc())
                .limit(10)
                .fetch();
    }
}
