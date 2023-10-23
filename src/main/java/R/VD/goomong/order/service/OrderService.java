package goomong.order.service;

import goomong.member.exception.NotFoundMember;
import goomong.member.model.Member;
import goomong.member.repository.MemberRepository;
import goomong.order.dto.request.RequestOrderDto;
import goomong.order.dto.response.ResponseOrderDto;
import goomong.order.exception.NotExistOrder;
import goomong.order.model.Order;
import goomong.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    public List<ResponseOrderDto> getAllOrderList() {
        return orderRepository.findAll().stream().map(ResponseOrderDto::new).toList();
    }

    public List<ResponseOrderDto> findByMemberOrderList(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            throw new NotFoundMember("존재하지 않는 사용자입니다");
        }

        Member targetMember = member.get();
        List<Order> orderList = targetMember.getOrderList();
        return orderList.stream().map(ResponseOrderDto::new).toList();
    }

    public ResponseOrderDto findByOrderId(Long id) {
        Order order = findOrder(id);
        return new ResponseOrderDto(order);
    }

    // 주문 생성
    public void createNewOrder(RequestOrderDto orderDto) {
        Order order = orderDto.toEntity();
        Optional<Member> customer = memberRepository.findById(orderDto.getMemberId());
        if (customer.isEmpty()) {
            throw new NotFoundMember("존재하지 않는 사용자입니다");
        }
        Member member = customer.get();
        order.setMember(member);
        order.calculatePrice();
        orderRepository.save(order);
        member.getOrderList().add(order);
    }

    // 작업 시작
    public void working(Long id) {
        Order order = findOrder(id);
        order.working();
    }

    // 작업 완료
    public void jobsFinish(Long id) {
        Order order = findOrder(id);
        order.jobsFinish();
    }

    // 환불 신청
    public void refund(Long id) {
        Order order = findOrder(id);
        order.refund();
    }

    // 환불 완료
    public void refundComplete(Long id) {
        Order order = findOrder(id);
        order.refundComplete();
    }

    // 중복 코드
    private Order findOrder(Long id) {
        Optional<Order> findOrder = orderRepository.findById(id);
        if (findOrder.isEmpty()) {
            throw new NotExistOrder("존재하지 않는 주문입니다");
        }
        return findOrder.get();
    }
}
