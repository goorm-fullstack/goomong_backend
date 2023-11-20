package R.VD.goomong.order.controller;

import R.VD.goomong.order.dto.request.RequestOrderDto;
import R.VD.goomong.order.dto.request.RequestSearchDto;
import R.VD.goomong.order.dto.response.ResponseOrderDto;
import R.VD.goomong.order.dto.response.ResponsePageOrderDto;
import R.VD.goomong.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order", description = "주문 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    @Operation(
            summary = "주문 리스트 출력",
            description = "전체 주문 리스트를 출력합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "리스트 조회 성공",
                            content = @Content(schema = @Schema(implementation = ResponseOrderDto.class)))
            }
    )
    @GetMapping("/list")
    public ResponseEntity<List<ResponseOrderDto>> getAll() {
        return ResponseEntity.ok(orderService.getAllOrderList());
    }

    @Operation(
            summary = "주문 출력",
            description = "특정 사용자의 주문 리스트를 출력합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "리스트 조회 성공",
                            content = @Content(schema = @Schema(implementation = ResponseOrderDto.class)))
            }
    )
    @GetMapping("/member/{id}/list")
    public ResponseEntity<ResponsePageOrderDto> findByMemberOrder(@PathVariable Long id, int page, int pageSize) {
        RequestSearchDto searchDto = new RequestSearchDto(page, pageSize);
        return ResponseEntity.ok(orderService.findByMemberOrderList(id, searchDto));
    }

    @Operation(
            summary = "주문 출력",
            description = "특정 주문를 출력합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(schema = @Schema(implementation = ResponseOrderDto.class)))
            }
    )
    @GetMapping("/find/{id}")
    public ResponseEntity<ResponseOrderDto> findByOrderId(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findByOrderId(id));
    }

    @Operation(
            summary = "환불 신청",
            description = "특정 주문에 대해서 환불을 신청합니다."
    )
    @PostMapping("/refund/{id}")
    public ResponseEntity<String> refund(@PathVariable Long id) {
        orderService.refund(id);
        return ResponseEntity.ok("환불이 신청되었습니다");
    }

    @Operation(
            summary = "환불 완료",
            description = "특정 주문에 대해서 환불 완료로 상태를 변경합니다."
    )
    @PostMapping("/refund/complete/{id}")
    public ResponseEntity<String> refundComplete(@PathVariable Long id) {
        orderService.refundComplete(id);
        return ResponseEntity.ok("환불 완료");
    }

    @Operation(
            summary = "작업중",
            description = "특정 주문의 상태를 작업중으로 변경합니다."
    )
    @PostMapping("/working/{id}")
    public ResponseEntity<String> working(@PathVariable Long id) {
        orderService.working(id);
        return ResponseEntity.ok("작업이 시작되었습니다.");
    }

    @Operation(
            summary = "완료",
            description = "특정 주문 처리가 완료되었다고 변경합니다."
    )
    @PostMapping("/complete/{id}")
    public ResponseEntity<String> jobsFinish(@PathVariable Long id) {
        orderService.jobsFinish(id);
        return ResponseEntity.ok("작업이 완료되었습니다.");
    }

    @PostMapping("/success")
    public ResponseEntity<String> orderComplete(@RequestBody RequestOrderDto orderDto) {
        orderService.createNewOrder(orderDto);
        return ResponseEntity.ok("주문이 완료되었습니다");
    }
}
