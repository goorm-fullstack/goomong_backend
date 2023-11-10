package R.VD.goomong.ranking.service;

import R.VD.goomong.item.model.ItemCategory;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.ranking.dto.response.ResponseRanking;
import R.VD.goomong.ranking.model.Ranking;
import R.VD.goomong.ranking.model.RankingPeriod;
import R.VD.goomong.ranking.repository.RankingRepository;
import R.VD.goomong.ranking.repository.RankingRepositorySupport;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;
    private final RankingRepositorySupport rankingRepositorySupport;

    public List<ResponseRanking> getRanking(RankingPeriod period) {

        List<Ranking> rankings = rankingRepository.findByPeriod(period);

        return rankings.stream().map(ResponseRanking::new).toList();

    }

    @Transactional
    @Scheduled(cron = "1 0 0 * * *")
    public void updateDailyRankings() {
        LocalDateTime startOfYesterday = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                .minusDays(1)
                .truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endOfYesterday = startOfYesterday.plusDays(1);

        updateRankings(startOfYesterday, endOfYesterday, RankingPeriod.DAY);
    }

    @Transactional
    @Scheduled(cron = "1 0 0 * * MON")
    public void updateWeeklyRankings() {
        LocalDateTime startOfLastWeek = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .minusWeeks(1)
                .truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endOfLastWeek = startOfLastWeek.plusWeeks(1).minusNanos(1);

        updateRankings(startOfLastWeek, endOfLastWeek, RankingPeriod.WEEK);
    }

    @Transactional
    @Scheduled(cron = "1 0 0 1 * *")
    public void updateMonthlyRankings() {
        LocalDateTime startOfLastMonth = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                .with(TemporalAdjusters.firstDayOfMonth())
                .minusMonths(1)
                .truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endOfLastMonth = startOfLastMonth.plusMonths(1).minusNanos(1);

        updateRankings(startOfLastMonth, endOfLastMonth, RankingPeriod.MONTH);
    }

    private void updateRankings(LocalDateTime startOfYesterday, LocalDateTime endOfYesterday, RankingPeriod period) {
        List<Tuple> tuples = rankingRepositorySupport
                .calculateSellerSalesCount(startOfYesterday, endOfYesterday);

        rankingRepository.deleteAllByPeriodAndDelDateIsNull(period);

        List<Ranking> rankings = tuples.stream().map(tuple -> {
            Member seller = tuple.get(0, Member.class);
            ItemCategory itemCategory = tuple.get(1, ItemCategory.class);
            Long salesCount = tuple.get(2, Long.class);

            return Ranking.builder()
                    .member(seller)
                    .itemCategory(itemCategory)
                    .salesCount(salesCount)
                    .period(period)
                    .build();
        }).toList();

        rankingRepository.saveAll(rankings);
    }
}
