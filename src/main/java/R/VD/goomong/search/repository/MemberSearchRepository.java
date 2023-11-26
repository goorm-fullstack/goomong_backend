package R.VD.goomong.search.repository;

import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.model.Seller;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.member.repository.SellerRepository;
import R.VD.goomong.ranking.exception.RankingNotFoundException;
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
    private final MemberRepository memberRepository;
    private final SellerRepository sellerRepository;

    public Page<ResponseFindMemberDTO> memberSearch(String keyword, String orderBy, String categoryTitle, Pageable pageable) {

        JPAQuery<Tuple> query = getMemberQuery(keyword, categoryTitle);

        if (orderBy != null && !orderBy.trim().isEmpty()) {
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
                    query.orderBy(item.countDistinct().desc());
                    break;
            }
        } else {
            query.orderBy(item.count().desc());
            query.orderBy(order.price.sum().desc());
            query.orderBy(review.id.count().desc());
            query.orderBy(review.rate.avg().desc());
        }

        List<Tuple> fetch = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<ResponseFindMemberDTO> members = fetch.stream().map(tuple -> ResponseFindMemberDTO.builder()
                        .memberId(tuple.get(item.member.id))
                        .memberName(tuple.get(item.member.memberId))
                        .saleSido(tuple.get(item.member.saleSido))
                        .saleInfo(tuple.get(item.member.saleInfo))
                        .transaction(tuple.get(item.count()))
                        .totalSales(tuple.get(order.price.sum()).longValue())
                        .reviewCount(tuple.get(review.id.count()))
                        .totalRating(tuple.get(review.rate.avg()))
                        .build())
                .toList();

        members.forEach(findMember -> {
            Long memberId = findMember.getMemberId();
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RankingNotFoundException("멤버 id " + memberId + " 는 찾을 수 없습니다."));
            Seller seller = sellerRepository.findByMemberId(member.getMemberId())
                    .orElseThrow(() -> new RankingNotFoundException("멤버 id " + memberId + " 는 찾을 수 없습니다."));
            findMember.setImagePath(seller.getImagePath());
        });

        JPAQuery<Long> countQuery = getMemberQuery(keyword, categoryTitle).select(member.count());
        Long total = countQuery.fetchOne();

        return new PageImpl<>(members, pageable, total);
    }

    private JPAQuery<Tuple> getMemberQuery(String keyword, String category) {
        JPAQuery<Tuple> query = jpaQueryFactory
                .selectDistinct(
                        item.member.id,
                        item.member.memberId,
                        item.member.saleSido, // 판매자 시/도
                        item.member.saleInfo,
                        item.count(),
                        order.price.sum(),
                        review.rate.avg(),
                        review.id.count()
                )
                .from(order)
                .join(order.orderItem, item)
                .join(item.member, member)
                .leftJoin(item.reviewList, review)
                .groupBy(order)
                .where(member.memberName.contains(keyword)
                        .or(member.saleInfo.contains(keyword))
                        .and(member.delDate.isNull()));

        if (category != null && !category.trim().isEmpty()) {
            query.where(itemCategory.title.eq(category));
        }

        return query;
    }

}
