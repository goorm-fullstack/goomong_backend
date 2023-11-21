package R.VD.goomong.point.service;

import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.point.dto.request.RequestEarnPoint;
import R.VD.goomong.point.dto.request.RequestSpentPoint;
import R.VD.goomong.point.dto.response.ResponsePointHistory;
import R.VD.goomong.point.exception.PointNotFoundException;
import R.VD.goomong.point.model.Point;
import R.VD.goomong.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void earnPoints(RequestEarnPoint requestEarnPoint) {
        Long memberId = requestEarnPoint.getMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new PointNotFoundException("멤버 " + memberId + " 는 찾을 수 없습니다."));

        int earnAmount = (int) (requestEarnPoint.getPrice() * EARN_PERCENTAGE); // 주문 금액의 1%를 포인트로 적립
        Point point = new Point();
        point.earnPoints(earnAmount, requestEarnPoint.getDescription(), requestEarnPoint.getOrderNumber());
        point.setMember(member);
        pointRepository.save(point);
    }

    @Transactional
    public void spendPoints(RequestSpentPoint requestSpentPoint) {
        Long memberId = requestSpentPoint.getMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new PointNotFoundException("멤버 " + memberId + " 는 찾을 수 없습니다."));

        Point point = new Point();
        point.spendPoints(requestSpentPoint.getPoint(), requestSpentPoint.getDescription(), requestSpentPoint.getOrderNumber());
        point.setMember(member);
        pointRepository.save(point);
    }

}
