package R.VD.goomong.ranking.service;

import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.model.Seller;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.member.repository.SellerRepository;
import R.VD.goomong.ranking.dto.response.ResponseMonthTopRanking;
import R.VD.goomong.ranking.dto.response.ResponseTopRanking;
import R.VD.goomong.ranking.exception.RankingIllegalArgumentException;
import R.VD.goomong.ranking.exception.RankingNotFoundException;
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
import static R.VD.goomong.order.model.QOrder.order;
import static R.VD.goomong.review.model.QReview.review;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;
    private final RankingSupportRepository rankingSupportRepository;
    private final MemberRepository memberRepository;
    private final SellerRepository sellerRepository;

    public List<ResponseMonthTopRanking> getMonthRanking() {
        List<Ranking> topRankings = rankingRepository.findAll();

        // RankingType 순서대로 정렬하기 위한 순서 목록
        List<RankingType> order = Arrays.asList(RankingType.ORDER, RankingType.REVIEW, RankingType.SALES);

        List<ResponseMonthTopRanking> list = topRankings.stream()
                .sorted(Comparator.comparingInt(r -> order.indexOf(r.getRankingType())))
                .map(ResponseMonthTopRanking::new)
                .toList();

        list.forEach(ranking -> {
            Long memberId = ranking.getMemberId();
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RankingNotFoundException("멤버 id " + memberId + " 는 찾을 수 없습니다."));
            Seller seller = sellerRepository.findByMemberId(member.getMemberId())
                    .orElseThrow(() -> new RankingNotFoundException("멤버 id " + memberId + " 는 찾을 수 없습니다."));
            ranking.setImagePath(seller.getImagePath());
        });
        
        return list;
    }

    public List<ResponseTopRanking> getSellerRankings() {
        List<ResponseTopRanking> rankings = rankingSupportRepository.calculateSellerRanking().stream()
                .map(tuple -> ResponseTopRanking.builder()
                        .memberId(tuple.get(item.member.id))
                        .memberName(tuple.get(item.member.memberName))
                        .saleSido(tuple.get(item.member.saleSido))
                        .transaction(tuple.get(item.count()))
                        .totalSales(tuple.get(order.price.sum()).longValue())
                        .reviewCount(tuple.get(review.id.count()))
                        .totalRating(tuple.get(review.rate.avg()))
                        .build())
                .toList();

        rankings.forEach(ranking -> {
            Long memberId = ranking.getMemberId();
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RankingNotFoundException("멤버 id " + memberId + " 는 찾을 수 없습니다."));
            Seller seller = sellerRepository.findByMemberId(member.getMemberId())
                    .orElseThrow(() -> new RankingNotFoundException("멤버 id " + memberId + " 는 찾을 수 없습니다."));
            ranking.setImagePath(seller.getImagePath());
        });

        return rankings;
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