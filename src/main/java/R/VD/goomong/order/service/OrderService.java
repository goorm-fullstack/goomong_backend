package R.VD.goomong.order.service;

import R.VD.goomong.global.model.PageInfo;
import R.VD.goomong.item.exception.NotFoundItem;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.model.Status;
import R.VD.goomong.item.repository.ItemRepository;
import R.VD.goomong.member.exception.NotFoundMember;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.order.dto.request.RequestOrderDto;
import R.VD.goomong.order.dto.request.RequestPayOrderDto;
import R.VD.goomong.order.dto.request.RequestSearchDto;
import R.VD.goomong.order.dto.response.ResponseOrderDto;
import R.VD.goomong.order.dto.response.ResponsePageOrderDto;
import R.VD.goomong.order.exception.InvalidOrderType;
import R.VD.goomong.order.exception.NotExistOrder;
import R.VD.goomong.order.model.Order;
import R.VD.goomong.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

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

    public ResponsePageOrderDto<List<ResponseOrderDto>> findByMemberOrderList(Long memberId, RequestSearchDto searchDto) {
        int page = searchDto.getPage() - 1;
        int pageSize = searchDto.getPageSize();
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Optional<Member> member = memberRepository.findById(memberId);

        if (member.isEmpty()) {
            throw new NotFoundMember("존재하지 않는 사용자입니다");
        }

        Member targetMember = member.get();
        List<Order> orderList = targetMember.getOrderList();

        // List to Page
        int end = Math.min((page + pageRequest.getPageSize()), orderList.size());
        Page<Order> orders = new PageImpl<>(orderList.subList(page, end), pageRequest, orderList.size());

        PageInfo pageInfo = PageInfo.builder()
                .page(page)
                .size(pageSize)
                .totalElements(orders.getTotalElements())
                .totalPage(orders.getTotalPages())
                .build();

        List<Order> pagingOrderList = orders.getContent();
        return new ResponsePageOrderDto<>(pagingOrderList.stream().map(ResponseOrderDto::new).toList(), pageInfo);
    }

    public ResponseOrderDto findByOrderId(Long id) {
        Order order = findOrder(id);
        return new ResponseOrderDto(order);
    }

    // 주문 생성
    public void createNewOrder(RequestOrderDto orderDto) {
        orderDto.setOrderNumber(generateRandomNumberWithDate());
        Order order = orderDto.toEntity();
        Optional<Member> customer = memberRepository.findById(orderDto.getMemberId());
        if (customer.isEmpty()) {
            throw new NotFoundMember("존재하지 않는 사용자입니다");
        }
        // 주문자 등록
        Member member = customer.get();
        order.setMember(member);

        List<Item> itemList = getItems(orderDto);

        order.setOrderItem(itemList);
        Order save = orderRepository.save(order);
        member.getOrderList().add(save);
    }

    // 주문 생성

    public void createNewOrder(RequestPayOrderDto orderDto) {
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
        Order save = orderRepository.save(order);
        member.getOrderList().add(save);
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

    // 주문한 아이템을 찾아서 주문에 반영
    private List<Item> getItems(RequestOrderDto orderDto) {
        //주문할 아이템 등록
        //만약 대상 아이템이 판매 타입인 경우 예외 발생
        List<Item> itemList = new ArrayList<>();
        for (Long itemId : orderDto.getOrderItem()) {
            Optional<Item> item = itemRepository.findById(itemId);
            if (item.isEmpty())
                throw new NotFoundItem();

            Item findItem = item.get();

            if (findItem.getStatus().equals(Status.SALE))
                throw new InvalidOrderType();

            itemList.add(findItem);
        }
        return itemList;
    }

    private Order findOrder(Long id) {
        Optional<Order> findOrder = orderRepository.findById(id);
        if (findOrder.isEmpty()) {
            throw new NotExistOrder("존재하지 않는 주문입니다");
        }
        return findOrder.get();
    }

    // 주문 번호 생성 로직입니다.
    private String generateRandomNumberWithDate() {
        // 현재 날짜 정보를 포함한 문자열 생성 (yyyyMMdd 형식)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String currentDate = dateFormat.format(new Date());
        // 6자리의 임의의 숫자열 생성
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000; // 100000부터 999999까지의 난수 생성
        // 현재 날짜 정보와 6자리의 임의의 숫자열을 조합하여 16자리의 숫자열 생성
        return currentDate + randomNumber;
    }
}
