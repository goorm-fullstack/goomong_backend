package R.VD.goomong.search.repository;

import R.VD.goomong.item.model.Item;
import R.VD.goomong.order.model.Status;
import R.VD.goomong.ranking.dto.response.ResponseTopRanking;
import R.VD.goomong.search.dto.response.ResponseFindMemberDTO;
import com.querydsl.core.Tuple;
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
import static R.VD.goomong.member.model.QMember.member;
import static R.VD.goomong.order.model.QOrder.order;
import static R.VD.goomong.review.model.QReview.review;

@Repository
@RequiredArgsConstructor
public class MemberSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<ResponseFindMemberDTO> memberSearch(String keyword, String orderBy, String categoryTitle, Pageable pageable) {

        JPAQuery<Tuple> query = getMemberQuery(keyword, categoryTitle);

        switch (orderBy) {
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

        List<Tuple> fetch = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<ResponseFindMemberDTO> members = fetch.stream().map(tuple -> ResponseFindMemberDTO.builder()
                        .memberId(tuple.get(member.id))
                        .memberName(tuple.get(member.memberName))
                        .categoryTitle(tuple.get(itemCategory.title))
                        .itemCount(tuple.get(item.countDistinct()))
                        .totalSales(tuple.get(order.price.sum()).longValue())
                        .reviewCount(tuple.get(review.id.count()))
                        .build())
                .toList();

        JPAQuery<Long> countQuery = getMemberQuery(keyword, categoryTitle).select(member.count());
        Long total = countQuery.fetchOne();

        return new PageImpl<>(members, pageable, total);
    }

    private JPAQuery<Tuple> getMemberQuery(String keyword, String category) {
        JPAQuery<Tuple> query = jpaQueryFactory
                .select(member, itemCategory.title, item.countDistinct(), order.price.sum(), review.id.count())
                .from(order)
                .join(order.member, member)
                .join(order.orderItem, item)
                .leftJoin(item.reviewList, review)
                .join(item.itemCategories, itemCategory)
                .where(order.status.eq(Status.COMPLETE)
                        .and(member.memberName.eq(keyword)));

        if (category != null && !category.trim().isEmpty()) {
            query.where(itemCategory.title.eq(category));
        }

        return query;
    }

}
