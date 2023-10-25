package R.VD.goomong.order.service;

import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.repository.ItemRepository;
import R.VD.goomong.member.exception.NotFoundMember;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.order.dto.request.RequestOrderDto;
import R.VD.goomong.order.dto.response.ResponseOrderDto;
import R.VD.goomong.order.exception.NotExistOrder;
import R.VD.goomong.order.model.Order;
import R.VD.goomong.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

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
        // 주문자 등록
        Member member = customer.get();
        order.setMember(member);

        //주문할 아이템 등록
        List<Item> itemList = new ArrayList<>();
        for (Long itemId : orderDto.getOrderItem()) {
            Optional<Item> item = itemRepository.findById(itemId);
            item.ifPresent(itemList::add);
        }
        order.setOrderItem(itemList);

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
