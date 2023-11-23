package R.VD.goomong.ranking.service;

import R.VD.goomong.member.model.Member;
import R.VD.goomong.ranking.dto.response.ResponseMonthTopRanking;
import R.VD.goomong.ranking.dto.response.ResponseTopRanking;
import R.VD.goomong.ranking.exception.RankingIllegalArgumentException;
import R.VD.goomong.ranking.model.Ranking;
import R.VD.goomong.ranking.model.RankingType;
import R.VD.goomong.ranking.repository.RankingRepository;
import R.VD.goomong.ranking.repository.RankingSupportRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static R.VD.goomong.item.model.QItem.item;
import static R.VD.goomong.item.model.QItemCategory.itemCategory;
import static R.VD.goomong.member.model.QMember.member;
import static R.VD.goomong.order.model.QOrder.order;
import static R.VD.goomong.review.model.QReview.review;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;
    private final RankingSupportRepository rankingSupportRepository;

    public List<ResponseMonthTopRanking> getMonthRanking() {
        List<Ranking> topRankings = rankingRepository.findAll();

        // RankingType 순서대로 정렬하기 위한 순서 목록
        List<RankingType> order = Arrays.asList(RankingType.ORDER, RankingType.REVIEW, RankingType.SALES);

        return topRankings.stream()
                .sorted(Comparator.comparingInt(r -> order.indexOf(r.getRankingType())))
                .map(ResponseMonthTopRanking::new)
                .toList();
    }

    public List<ResponseTopRanking> getSellerRankings(String categoryTitle, String sortBy) {
        return rankingSupportRepository.calculateSellerRanking(categoryTitle, sortBy).stream()
                .map(tuple -> ResponseTopRanking.builder()
                        .memberId(tuple.get(member.id))
                        .memberName(tuple.get(member.memberName))
                        .categoryTitle(tuple.get(itemCategory.title))
                        .itemCount(tuple.get(item.countDistinct()))
                        .totalSales(tuple.get(order.price.sum()).longValue())
                        .reviewCount(tuple.get(review.id.count()))
                        .build())
                .toList();
    }

    @Transactional
    @Scheduled(cron = "1 0 0 1 * *")
    public void updateMonthlyRankings() {
        LocalDateTime startOfLastMonth = LocalDateTime.now()
                .with(TemporalAdjusters.firstDayOfMonth())
                .minusMonths(1)
                .truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endOfLastMonth = startOfLastMonth.plusMonths(1).minusNanos(1);

        updateRankings(startOfLastMonth, endOfLastMonth);
    }

    private void updateRankings(LocalDateTime startOfLastMonth, LocalDateTime endOfLastMonth) {
        for (RankingType type : RankingType.values()) {
            // 해당 타입의 기존 랭킹 데이터를 soft delete 처리
            rankingRepository.deleteAllByRankingTypeAndDelDateIsNull(type);

            // 새로운 랭킹 데이터 계산 및 저장
            List<Ranking> newRankings = calculateRankingForType(type, startOfLastMonth, endOfLastMonth);
            rankingRepository.saveAll(newRankings);
        }
    }

    private List<Ranking> calculateRankingForType(RankingType type, LocalDateTime start, LocalDateTime end) {
        List<Ranking> rankings = new ArrayList<>();

        switch (type) {
            case ORDER:
                List<Tuple> orderRankings = rankingSupportRepository.calculateTop5SellersByOrderCount(start, end);
                orderRankings.forEach(tuple -> rankings.add(createRanking(tuple, RankingType.ORDER)));
                break;

            case REVIEW:
                List<Tuple> reviewRankings = rankingSupportRepository.calculateTop5SellersByReviewCount(start, end);
                reviewRankings.forEach(tuple -> rankings.add(createRanking(tuple, RankingType.REVIEW)));
                break;

            case SALES:
                List<Tuple> salesRankings = rankingSupportRepository.calculateTop5SellersBySalesAmount(start, end);
                salesRankings.forEach(tuple -> rankings.add(createRanking(tuple, RankingType.SALES)));
                break;

            default:
                throw new RankingIllegalArgumentException("알 수 없는 타입: " + type);
        }

        return rankings;
    }

    private Ranking createRanking(Tuple tuple, RankingType type) {
        Member member = tuple.get(0, Member.class);
        Long count = tuple.get(1, Long.class);
        return Ranking.builder()
                .member(member)
                .count(count)
                .rankingType(type)
                .build();
    }
}