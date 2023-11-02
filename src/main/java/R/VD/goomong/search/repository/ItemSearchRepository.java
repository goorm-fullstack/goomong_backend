package R.VD.goomong.search.repository;

import R.VD.goomong.item.model.Item;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static R.VD.goomong.item.model.QItem.item;
import static R.VD.goomong.item.model.QItemCategory.itemCategory;
import static R.VD.goomong.order.model.QOrder.order;

@Repository
@RequiredArgsConstructor
public class ItemSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Item> itemSearch(String keyword, String orderBy, String category, Pageable pageable) {

//        BooleanBuilder builder = new BooleanBuilder();
//
//        BooleanExpression expr1 = Expressions.booleanTemplate("MATCH({0}) AGAINST ({1})", item.title, keyword);
//        BooleanExpression expr2 = Expressions.booleanTemplate("MATCH({0}) AGAINST ({1})", item.describe, keyword);
//        BooleanExpression expr3 = Expressions.booleanTemplate("MATCH({0}) AGAINST ({1})", itemCategory.title, keyword);
//        BooleanExpression expr4 = Expressions.booleanTemplate("MATCH({0}) AGAINST ({1})", item.member.name, keyword);
//
//        JPAQuery<Item> query = jpaQueryFactory
//                .selectFrom(item)
//                .leftJoin(item.itemCategories, itemCategory)
//                .where(expr1.or(expr2).or(expr3).or(expr4));

        // 정확도 계산
//        .orderBy(Expressions.stringTemplate("MATCH({0}) AGAINST ({1})", item.title, keyword).desc());

        JPAQuery<Item> query = jpaQueryFactory
                .selectFrom(item)
                .leftJoin(item.itemCategories, itemCategory)
                .leftJoin(order.orderItem, item)
                .where(
                        item.title.contains(keyword)
                                .or(item.describe.contains(keyword))
                                .or(itemCategory.title.contains(keyword))
                                .or(item.member.name.contains(keyword))
                );

        if (category != null && !category.isEmpty())
            query.where(itemCategory.title.eq(category));

        switch (orderBy) {
            case "price":
                query.orderBy(item.price.desc());
                break;
            case "orderCount":
                query.orderBy(order.count().desc());
            case "time":
                query.orderBy(item.regDate.desc());
                break;
            case "rate":
                query.orderBy(item.rate.desc());
                break;
            default:
                query.orderBy(item.title.desc());
        }

        return new PageImpl<>(query.fetch(), pageSize, offset)
    }
}