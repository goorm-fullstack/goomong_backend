package goomong.order.controller;

import goomong.order.dto.request.RequestOrderDto;
import goomong.order.dto.response.ResponseOrderDto;
import goomong.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/list")
    public ResponseEntity<List<ResponseOrderDto>> getAll() {
        return ResponseEntity.ok(orderService.getAllOrderList());
    }

    @GetMapping("/member/{id}/list")
    public ResponseEntity<List<ResponseOrderDto>> findByMemberOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findByMemberOrderList(id));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ResponseOrderDto> findByOrderId(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findByOrderId(id));
    }

    // 생성할 때 페이 로직을 넣으면 될까나?
    @PostMapping("/create")
    public ResponseEntity<String> createNewOrder(@RequestBody RequestOrderDto orderDto) {
        orderService.createNewOrder(orderDto);
        return ResponseEntity.ok("주문이 완료되었습니다");
    }

    @PostMapping("/refund/{id}")
    public ResponseEntity<String> refund(@PathVariable Long id) {
        orderService.refund(id);
        return ResponseEntity.ok("환불이 신청되었습니다");
    }

    @PostMapping("/refund/complete/{id}")
    public ResponseEntity<String> refundComplete(@PathVariable Long id) {
        orderService.refundComplete(id);
        return ResponseEntity.ok("환불 완료");
    }

    @PostMapping("/working/{id}")
    public ResponseEntity<String> working(@PathVariable Long id) {
        orderService.working(id);
        return ResponseEntity.ok("작업이 시작되었습니다.");
    }

    @PostMapping("/complete/{id}")
    public ResponseEntity<String> jobsFinish(@PathVariable Long id) {
        orderService.jobsFinish(id);
        return ResponseEntity.ok("작업이 완료되었습니다.");
    }
}
