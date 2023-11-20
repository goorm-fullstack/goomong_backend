package R.VD.goomong.point.service;

import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.point.model.Point;
import R.VD.goomong.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {

    private static final double EARN_PERCENTAGE = 0.01; // 기본 포인트 적립 비율

    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void earnPoints(Long memberId, int amount, String itemDescription, String orderNumber) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));

        int earnAmount = (int)(amount * EARN_PERCENTAGE); // 주문 금액의 1%를 포인트로 적립
        Point point = new Point();
        point.earnPoints(earnAmount, itemDescription, orderNumber);
        point.setMember(member);
        member.addPoints(point);
        pointRepository.save(point);
    }

    @Transactional
    public void spendPoints(Long memberId, int amount, String itemDescription, String orderNumber) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));

        Point point = new Point();
        point.spendPoints(amount, itemDescription, orderNumber);
        point.setMember(member);
        member.addPoints(point);
        pointRepository.save(point);

    }

    @Transactional(readOnly = true)
    public int getTotalPoints(Long memberId) {
        Integer totalPoints = pointRepository.sumPointsByMember_Id(memberId);

        return totalPoints != null ? totalPoints : 0;
    }

    @Transactional(readOnly = true)
    public List<Point> getPointHistory(Long memberId) {

        List<Point> points = pointRepository.findByMember_Id(memberId);
        return points;
    }
}
