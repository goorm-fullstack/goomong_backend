package R.VD.goomong.item.controller;

import R.VD.goomong.item.dto.request.RequestItemDto;
import R.VD.goomong.item.dto.request.UpdateItemDto;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.dto.response.ResponseItemPageDto;
import R.VD.goomong.item.dto.response.ResponseNonSaleItemDto;
import R.VD.goomong.item.service.ItemService;
import R.VD.goomong.post.dto.response.ResponsePostDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Tag(name = "Item", description = "아이템 API")
@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

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
    public ResponseEntity<List<ResponseItemDto>> getItemList() {
        return ResponseEntity.ok(itemService.findAll());
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

    // 컨틀롤러에도 리스트 출력 시 카테고리와 지역에 따라 다르게 출력되도록 추가되어있으니 확인 부탁드려요 - @배진환

    /**
     * @param orderBy   : title, price 같이 엔티티 기반 프로퍼티 명
     * @param direction : asc, desc
     * @param pageable  : 페이지 번호
     */
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
    public ResponseEntity<ResponseItemPageDto> getItemListBySale(@RequestParam Optional<String> orderBy, @RequestParam Optional<String> direction,
                                                                 Pageable pageable, @RequestParam(required = false) String categoryName, @RequestParam(required = false) String region) {
        log.info("categoryName={}, region={}", categoryName, region);
        pageable = getPageable(orderBy, direction, pageable);

        return ResponseEntity.ok(itemService.findAllBySale(pageable, categoryName, region));
    }

    @Operation(
            summary = "판매 상태가 아닌 아이템 리스트 출력",
            description = "기부 아이템 리스트를 출력합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "리스트 조회 성공",
                            content = @Content(schema = @Schema(implementation = ResponseNonSaleItemDto.class)))
            }
    )
    @GetMapping("/list/give")
    public ResponseEntity<ResponseItemPageDto> getItemListByGive(@RequestParam Optional<String> orderBy, @RequestParam Optional<String> direction,
                                                                 Pageable pageable, @RequestParam(required = false) String categoryName, @RequestParam(required = false) String region) {
        log.info("categoryName={}, region={}", categoryName, region);
        pageable = getPageable(orderBy, direction, pageable);

        return ResponseEntity.ok(itemService.findAllByGive(pageable, categoryName, region));
    }

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
    public ResponseEntity<ResponseItemPageDto> getItemListByWanted(@RequestParam Optional<String> orderBy, @RequestParam Optional<String> direction,
                                                                   Pageable pageable, @RequestParam(required = false) String categoryName, @RequestParam(required = false) String region) {
        log.info("categoryName={}, region={}", categoryName, region);
        pageable = getPageable(orderBy, direction, pageable);

        return ResponseEntity.ok(itemService.findAllByWanted(pageable, categoryName, region));
    }

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
    public ResponseEntity<ResponseItemPageDto> getItemListByExchange(@RequestParam Optional<String> orderBy, @RequestParam Optional<String> direction,
                                                                     Pageable pageable, @RequestParam(required = false) String categoryName, @RequestParam(required = false) String region) {
        log.info("categoryName={}, region={}", categoryName, region);
        pageable = getPageable(orderBy, direction, pageable);

        return ResponseEntity.ok(itemService.findAllByExchange(pageable, categoryName, region));
    }

    @Operation(summary = "hot 아이템 리스트")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))))
    @GetMapping("/hot")
    public ResponseEntity<ResponseItemPageDto> hotPost() {
        ResponseItemPageDto hotItem = itemService.getHotItem();
        return new ResponseEntity<>(hotItem, HttpStatus.OK);
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

    private Pageable getPageable(Optional<String> orderBy, Optional<String> direction, Pageable pageable) {
        if (orderBy.isPresent() && direction.isPresent()) {
            Sort.Direction dir = Sort.Direction.fromString(direction.get());
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(dir, orderBy.get()));
        }
        return pageable;
    }
}