package R.VD.goomong.point.service;

import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.point.dto.response.ResponsePointHistory;
import R.VD.goomong.point.model.Point;
import R.VD.goomong.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {

    private static final double EARN_PERCENTAGE = 0.01; // 기본 포인트 적립 비율

    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public int getTotalPoints(Long memberId) {
        Integer totalPoints = pointRepository.sumPointsByMember_Id(memberId);

        return totalPoints != null ? totalPoints : 0;
    }

    @Transactional(readOnly = true)
    public List<ResponsePointHistory> getPointHistory(Long memberId) {

        return pointRepository.findByMember_Id(memberId).stream()
                .map(ResponsePointHistory::new)
                .toList();
    }

    @Transactional
    public int getExpiringPoints(Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthOneWeekAgo = now.minusMonths(1).minusWeeks(1).toLocalDate().atStartOfDay();
        LocalDateTime oneMonthAgo = now.minusMonths(1).toLocalDate().atStartOfDay();

        Integer expiringPoints = pointRepository.findExpiringPointsForMember(memberId, oneMonthOneWeekAgo, oneMonthAgo);
        return expiringPoints != null ? expiringPoints : 0;
    }

    // 유효기간에 따른 포인트 차감
    @Scheduled(cron = "1 0 0 * * *")  // 매일 자정에 실행
    public void removeExpiredPoints() {
        LocalDateTime oneMonthAgoStartOfDay = LocalDateTime.now().minusMonths(1).toLocalDate().atStartOfDay();
        LocalDateTime nextDayStartOfDay = oneMonthAgoStartOfDay.plusDays(1);

        List<Object[]> expiredPointsData = pointRepository.sumExpiredPointsByMember(oneMonthAgoStartOfDay, nextDayStartOfDay);
        for (Object[] data : expiredPointsData) {
            Member member = (Member) data[0];
            int totalExpiredPoints = ((Number) data[1]).intValue();

            if (totalExpiredPoints > 0)
                expirePoint(member, totalExpiredPoints, "포인트 유효기간 만료", "");
        }
    }

    // 적립
    @Transactional
    public void earnPoint(Member member, int price, String description, String orderNumber) {
        int earnAmount = (int) (price * EARN_PERCENTAGE); // 주문 금액의 1%를 포인트로 적립
        Point point = new Point();
        point.earnPoints(earnAmount, description, orderNumber);
        point.setMember(member);
        pointRepository.save(point);
    }

    // 사용
    @Transactional
    public void spendPoint(Member member, int amount, String description, String orderNumber) {
        Point point = new Point();
        point.spendPoints(amount, description, orderNumber);
        point.setMember(member);
        pointRepository.save(point);
    }

    // 환불에 따른 적립금 적립 취소
    @Transactional
    public void cancelPoint(Member member, int price, String description, String orderNumber) {
        int cancelAmount = (int) (price * EARN_PERCENTAGE);

        Point point = new Point();
        point.spendPoints(cancelAmount, description, orderNumber);
        point.setMember(member);
        pointRepository.save(point);
    }

    // 환불에 따른 포인트 사용 취소
    @Transactional
    public void redemptionPoint(Member member, int amount, String description, String orderNumber) {
        Point point = new Point();
        point.earnPoints(amount, description, orderNumber);
        point.setMember(member);
        pointRepository.save(point);
    }

    // 만료 포인트 적용
    @Transactional
    public void expirePoint(Member member, int amount, String description, String orderNumber) {
        Point point = new Point();
        point.spendPoints(amount, description, orderNumber);
        point.setMember(member);
        pointRepository.save(point);
    }

}
