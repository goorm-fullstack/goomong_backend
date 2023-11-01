package R.VD.goomong.search.repository;

import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.model.Item;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static R.VD.goomong.item.model.QItem.item;
import static R.VD.goomong.item.model.QItemCategory.itemCategory;

@Repository
@RequiredArgsConstructor
public class ItemSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<ResponseItemDto> itemSearch(String keyword, String orderBy) {

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

        JPAQuery<Item> query = jpaQueryFactory
                .selectFrom(item)
                .leftJoin(item.itemCategories, itemCategory)
                .where(
                        item.title.contains(keyword)
                                .or(item.describe.contains(keyword))
                                .or(itemCategory.title.contains(keyword))
                                .or(item.member.name.contains(keyword))
                );

        switch (orderBy) {
            case "title":
                query.orderBy(item.title.desc());
                break;
            case "time":
                query.orderBy();
        }

        List<Item> findItem = query.fetch();

        return findItem.stream().map(ResponseItemDto::new).toList();
    }
}