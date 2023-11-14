package R.VD.goomong.ranking.service;

import R.VD.goomong.member.model.Member;
import R.VD.goomong.ranking.dto.response.ResponseTopSellerRanking;
import R.VD.goomong.ranking.model.RankingType;
import R.VD.goomong.ranking.model.TopSellerRanking;
import R.VD.goomong.ranking.repository.RankingRepository;
import R.VD.goomong.ranking.repository.RankingSupportRepository;
import R.VD.goomong.ranking.repository.TopSellerRankingRepository;
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

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;
    private final TopSellerRankingRepository topSellerRankingRepository;
    private final RankingSupportRepository rankingSupportRepository;

    public List<ResponseTopSellerRanking> getRanking() {
        List<TopSellerRanking> topRankings = topSellerRankingRepository.findAll();

        // RankingType 순서대로 정렬하기 위한 순서 목록
        List<RankingType> order = Arrays.asList(RankingType.REVIEW, RankingType.ORDER, RankingType.SALES);

        return topRankings.stream()
                .sorted(Comparator.comparingInt(r -> order.indexOf(r.getRankingType())))
                .map(ResponseTopSellerRanking::new)
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
            topSellerRankingRepository.deleteAllByRankingTypeAndDelDateIsNull(type);

            // 새로운 랭킹 데이터 계산 및 저장
            List<TopSellerRanking> newRankings = calculateRankingForType(type, startOfLastMonth, endOfLastMonth);
            topSellerRankingRepository.saveAll(newRankings);
        }
    }

    private List<TopSellerRanking> calculateRankingForType(RankingType type, LocalDateTime start, LocalDateTime end) {
        List<TopSellerRanking> rankings = new ArrayList<>();

        switch (type) {
            case ORDER:
                List<Tuple> orderRankings = rankingSupportRepository.calculateTop5SellersByOrderCount(start, end);
                orderRankings.forEach(tuple -> rankings.add(createTopSellerRanking(tuple, RankingType.ORDER)));
                break;

            case REVIEW:
                List<Tuple> reviewRankings = rankingSupportRepository.calculateTop5SellersByReviewCount(start, end);
                reviewRankings.forEach(tuple -> rankings.add(createTopSellerRanking(tuple, RankingType.REVIEW)));
                break;

            case SALES:
                List<Tuple> salesRankings = rankingSupportRepository.calculateTop5SellersBySalesAmount(start, end);
                salesRankings.forEach(tuple -> rankings.add(createTopSellerRanking(tuple, RankingType.SALES)));
                break;
        }

        return rankings;
    }

    private TopSellerRanking createTopSellerRanking(Tuple tuple, RankingType type) {
        Member member = tuple.get(0, Member.class);
        Long count = tuple.get(1, Long.class);
        return TopSellerRanking.builder()
                .member(member)
                .count(count)
                .rankingType(type)
                .build();
    }
}
