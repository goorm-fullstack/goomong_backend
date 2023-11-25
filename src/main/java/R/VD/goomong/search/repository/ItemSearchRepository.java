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

@Repository
@RequiredArgsConstructor
public class ItemSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Item> itemSearch(String keyword, String orderBy, String categoryTitle, Pageable pageable) {

        JPAQuery<Item> query = getItemQuery(keyword, categoryTitle);

        if (orderBy != null)
            switch (orderBy) {
                case "title":
                    query.orderBy(item.title.asc());
                case "price":
                    query.orderBy(item.price.desc());
                    break;
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

        JPAQuery<Long> countQuery = getItemQuery(keyword, categoryTitle).select(item.count());
        Long total = countQuery.fetchOne();

        return new PageImpl<>(items, pageable, total);
    }

    private JPAQuery<Item> getItemQuery(String keyword, String categoryTitle) {
        JPAQuery<Item> query = jpaQueryFactory
                .selectFrom(item)
                .leftJoin(item.itemCategories, itemCategory)
                .where(
                        item.title.contains(keyword)
                                .or(item.describe.contains(keyword))
                                .or(itemCategory.title.contains(keyword))
                                .or(item.member.memberName.contains(keyword))
                                .and(item.delDate.isNull())
                );
        if (categoryTitle != null && !categoryTitle.isEmpty())
            query.where(itemCategory.title.eq(categoryTitle));

        return query;
    }
}