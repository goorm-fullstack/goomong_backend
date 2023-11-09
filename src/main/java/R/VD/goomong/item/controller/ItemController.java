package R.VD.goomong.item.controller;

import R.VD.goomong.item.dto.request.RequestItemDto;
import R.VD.goomong.item.dto.request.UpdateItemDto;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.dto.response.ResponseNonSaleItemDto;
import R.VD.goomong.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Tag(name = "Item", description = "아이템 API")
@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    // 페이지네이션 수정 - (송정우);
    @Operation(
            summary = "아이템 리스트 출력",
            description = "아이템 리스트 출력.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "리스트 조회 성공",
                            content = @Content(schema = @Schema(implementation = ResponseItemDto.class)))
            }
    )
    @GetMapping("/list")
    public ResponseEntity<List<ResponseItemDto>> getItemList(@RequestParam Optional<String> orderBy,
                                                             @RequestParam Optional<String> direction,
                                                             Pageable pageable) {
        pageable = getPageable(orderBy, direction, pageable);

        Page<ResponseItemDto> all = itemService.findAll(pageable);

        return getListResponseEntity(all);
    }

    @Operation(
            summary = " 아이템 출력",
            description = "특정 인덱스 아이템 출력.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "아이템 조회 성공",
                            content = @Content(schema = @Schema(implementation = ResponseItemDto.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ResponseItemDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.findById(id));
    }

    @Operation(
            summary = "아이템 등록",
            description = "아이템을 등록합니다."
    )
    @PostMapping("/save")
    public ResponseEntity<String> writeGiveItem(MultipartFile[] multipartFiles, @Valid @RequestPart RequestItemDto itemDto) {
        itemService.save(itemDto, multipartFiles);
        return ResponseEntity.ok("작성이 완료되었습니다.");
    }

    // 페이지네이션 수정 - (송정우);
    @Operation(
            summary = "판매 아이템 리스트 출력",
            description = "판매 아이템 리스트 출력.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "리스트 조회 성공",
                            content = @Content(schema = @Schema(implementation = ResponseItemDto.class)))
            }
    )
    @GetMapping("/list/sale")
    public ResponseEntity<List<ResponseItemDto>> getItemListBySale(@RequestParam Optional<String> orderBy,
                                                                   @RequestParam Optional<String> direction,
                                                                   Pageable pageable) {

        pageable = getPageable(orderBy, direction, pageable);

        Page<ResponseItemDto> allBySale = itemService.findAllBySale(pageable);

        return getListResponseEntity(allBySale);

    }

    // 페이지네이션 수정 - (송정우);
    @Operation(
            summary = "판매 상태가 아닌 아이템 리스트 출력",
            description = "기부, 교환, 구인 아이템 리스트를 출력합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "리스트 조회 성공",
                            content = @Content(schema = @Schema(implementation = ResponseNonSaleItemDto.class)))
            }
    )
    @GetMapping("/list/non-sale")
    public ResponseEntity<List<ResponseNonSaleItemDto>> getItemListByGive(@RequestParam Optional<String> orderBy,
                                                                          @RequestParam Optional<String> direction,
                                                                          Pageable pageable) {
        pageable = getPageable(orderBy, direction, pageable);

        Page<ResponseNonSaleItemDto> allByGive = itemService.findAllByGive(pageable);

        return getListResponseEntity(allByGive);
    }

    // 페이지네이션 수정 - (송정우);
    @Operation(
            summary = "구인 아이템 리스트 출력",
            description = "구인 아이템을 조회해서 리스트 출력.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "리스트 조회 성공",
                            content = @Content(schema = @Schema(implementation = ResponseNonSaleItemDto.class)))
            }
    )
    @GetMapping("/list/wanted")
    public ResponseEntity<List<ResponseNonSaleItemDto>> getItemListByWanted(@RequestParam Optional<String> orderBy,
                                                                            @RequestParam Optional<String> direction,
                                                                            Pageable pageable) {

        pageable = getPageable(orderBy, direction, pageable);

        Page<ResponseNonSaleItemDto> allByWanted = itemService.findAllByWanted(pageable);

        return getListResponseEntity(allByWanted);
    }

    // 페이지네이션 수정 - (송정우);
    @Operation(
            summary = "교환 아이템 리스트 출력",
            description = "교환 아이템을 조회해서 리스트 출력.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "리스트 조회 성공",
                            content = @Content(schema = @Schema(implementation = ResponseNonSaleItemDto.class)))
            }
    )
    @GetMapping("/list/exchange")
    public ResponseEntity<List<ResponseNonSaleItemDto>> getItemListByExchange(@RequestParam Optional<String> orderBy,
                                                                              @RequestParam Optional<String> direction,
                                                                              Pageable pageable) {

        pageable = getPageable(orderBy, direction, pageable);

        Page<ResponseNonSaleItemDto> allByExchange = itemService.findAllByExchange(pageable);

        return getListResponseEntity(allByExchange);
    }

    @Operation(
            summary = "아이템 삭제",
            description = "특정 아이템을 삭제합니다. - SoftDelete"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok("삭제 완료");
    }

    @Operation(
            summary = "아이템 업데이트",
            description = "특정 아이템 정보를 수정합니다."
    )
    @PutMapping("/update")
    public ResponseEntity<String> updateItem(@RequestBody UpdateItemDto itemDto) {
        itemService.updateItem(itemDto);
        return ResponseEntity.ok("업데이트 완료");
    }

    // 조건에 맞게 페이지네이션 설정
    // 기본은 최신 시간순으로 정렬
    private Pageable getPageable(Optional<String> orderBy, Optional<String> direction, Pageable pageable) {
        if (orderBy.isPresent() && direction.isPresent()) {
            Sort.Direction dir = Sort.Direction.fromString(direction.get());
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(dir, orderBy.get()));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "regDate"));
        }
        return pageable;
    }

    // header에 PageNation 정보 추가
    private <T> ResponseEntity<List<T>> getListResponseEntity(Page<T> all) {
        long totalElements = all.getTotalElements();
        int totalPages = all.getTotalPages();
        return ResponseEntity.ok()
                .header("TotalPages", String.valueOf(totalPages))
                .header("TotalData", String.valueOf(totalElements))
                .body(all.getContent());
    }
}
