package R.VD.goomong.member.controller;

import R.VD.goomong.global.model.PageInfo;
import R.VD.goomong.member.dto.request.RequestSellerDto;
import R.VD.goomong.member.dto.response.ResponseSellerDto;
import R.VD.goomong.member.model.Seller;
import R.VD.goomong.member.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sellers")
@Slf4j
public class SellerController {

    private final SellerService sellerService;

    /**
     * 판매자 저장
     *
     * @param requestSellerDto 판매자 저장 정보
     * @return 완료 시 200
     */
    @Operation(summary = "판매자 저장")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping("/seller")
    public ResponseEntity<?> saveSeller(@RequestBody RequestSellerDto requestSellerDto) {
        log.info("requestSellerDto={}", requestSellerDto);
        sellerService.save(requestSellerDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 판매자 수정
     *
     * @param requestSellerDto 판매자 수정 정보
     * @return 수정된 내용
     */
    @Operation(summary = "판매자 수정")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseSellerDto.class)))
    @PutMapping("/seller")
    public ResponseEntity<ResponseSellerDto> updateSeller(@RequestBody RequestSellerDto requestSellerDto) {
        log.info("requestSellerDto={}", requestSellerDto);
        Seller update = sellerService.update(requestSellerDto);
        return ResponseEntity.ok(update.toResponseSellerDto());
    }

    /**
     * 판매자 삭제 - softDelete
     *
     * @param sellerId 삭제할 판매자 아이디
     * @return 완료 시 200
     */
    @Operation(summary = "판매자 삭제")
    @Parameter(name = "sellerId", description = "삭제할 판매자 아이디", example = "아이디")
    @ApiResponse(responseCode = "200", description = "성공")
    @DeleteMapping("/seller/{sellerId}")
    public ResponseEntity<?> deleteSeller(@PathVariable String sellerId) {
        sellerService.delete(sellerId);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 판매자 조회
     *
     * @param sellerId 조회할 판매자 아이디
     * @return 조회한 판매자
     */
    @Operation(summary = "특정 판매자 조회")
    @Parameter(name = "sellerId", description = "조회할 판매자 아이디", example = "아이디")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseSellerDto.class)))
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ResponseSellerDto> findOne(@PathVariable String sellerId) {
        Seller one = sellerService.findOne(sellerId);
        return ResponseEntity.ok(one.toResponseSellerDto());
    }

    /**
     * 전체 판매자 리스트 조회
     *
     * @param pageable  페이징
     * @param orderBy   정렬할 필드명
     * @param direction 오름차순, 내림차순
     * @return 조회한 리스트
     */
    @Operation(summary = "전체 판매자 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true),
            @Parameter(name = "orderBy", description = "정렬할 필드명", example = "필드명"),
            @Parameter(name = "direction", description = "오름차순, 내림차순", example = "desc")
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseSellerDto.class))))
    @GetMapping
    public ResponseEntity<List<ResponseSellerDto>> allSeller(@RequestParam(required = false) Optional<String> orderBy, @RequestParam(required = false) Optional<String> direction, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(required = false) String region) {
        log.info("orderBy={}, direction={}, region={}", orderBy, direction, region);
        pageable = getPageable(orderBy, direction, pageable);

        Page<Seller> all = sellerService.all(pageable, region);

        long totalElements = all.getTotalElements();
        int totalPages = all.getTotalPages();
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        List<ResponseSellerDto> list = new ArrayList<>();
        for (Seller seller : all.getContent()) {
            ResponseSellerDto responseSellerDto = seller.toResponseSellerDto();
            PageInfo pageInfo = PageInfo.builder()
                    .page(pageNumber)
                    .size(pageSize)
                    .totalPage(totalPages)
                    .totalElements(totalElements)
                    .build();
            ResponseSellerDto build = responseSellerDto.toBuilder()
                    .pageInfo(pageInfo)
                    .build();
            list.add(build);
        }
        return ResponseEntity.ok(list);
    }

    // 정렬에 따른 pageable settings
    private Pageable getPageable(Optional<String> orderBy, Optional<String> direction, Pageable pageable) {
        if (orderBy.isPresent() && direction.isPresent()) {
            Sort.Direction dir = Sort.Direction.fromString(direction.get());
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(dir, orderBy.get()));
        }
        return pageable;
    }
}
