package R.VD.goomong.search.repository;

import R.VD.goomong.item.model.Item;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static R.VD.goomong.item.model.QItem.item;
import static R.VD.goomong.item.model.QItemCategory.itemCategory;
import static R.VD.goomong.order.model.QOrder.order;

@Repository
@RequiredArgsConstructor
public class ItemSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Item> itemSearch(String keyword, String orderBy, String category, Pageable pageable) {

        JPAQuery<Item> query = getItemQuery(keyword, category);

        switch (orderBy) {
            case "title":
                query.orderBy(item.title.asc());
            case "price":
                query.orderBy(item.price.desc());
                break;
            case "orderNumber":
                query.orderBy(order.orderNumber.desc());
            case "time":
                query.orderBy(item.regDate.desc());
                break;
            case "rate":
                query.orderBy(item.rate.desc());
                break;
            default:
                query.orderBy(item.regDate.desc());
        }

        List<Item> items = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = getItemQuery(keyword, category).select(item.count());
        Long total = countQuery.fetchOne();

        return new PageImpl<>(items, pageable, total);
    }

    private JPAQuery<Item> getItemQuery(String keyword, String category) {
        JPAQuery<Item> query = jpaQueryFactory
                .selectFrom(item)
                .leftJoin(item.itemCategories, itemCategory)
                .leftJoin(order.orderItem, item)
                .where(
                        item.title.contains(keyword)
                                .or(item.description.contains(keyword))
                                .or(itemCategory.title.contains(keyword))
                                .or(item.member.memberName.contains(keyword))
                                .and(item.regDate.isNull())
                );
        if (category != null && !category.isEmpty())
            query.where(itemCategory.title.eq(category));

        return query;
    }
}