package R.VD.goomong.item.controller;

import R.VD.goomong.item.dto.request.RequestItemDto;
import R.VD.goomong.item.dto.request.RequestSearchDto;
import R.VD.goomong.item.dto.request.UpdateItemDto;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.dto.response.ResponseItemPageDto;
import R.VD.goomong.item.dto.response.ResponseNonSaleItemDto;
import R.VD.goomong.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Item", description = "아이템 API")
@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
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
    public ResponseEntity<ResponseItemPageDto> getItemList(int page, int pageSize) {
        RequestSearchDto searchDto = new RequestSearchDto(page, pageSize);
        return ResponseEntity.ok(itemService.findAll(searchDto));
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
    public ResponseEntity<ResponseItemPageDto> getItemListBySale(int page, int pageSize) {
        RequestSearchDto searchDto = new RequestSearchDto(page, pageSize);
        return ResponseEntity.ok(itemService.findAllBySale(searchDto));
    }

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
    @GetMapping("/list/give")
    public ResponseEntity<ResponseItemPageDto> getItemListByGive(int page, int pageSize) {
        RequestSearchDto searchDto = new RequestSearchDto(page, pageSize);
        return ResponseEntity.ok(itemService.findAllByGive(searchDto));
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
    public ResponseEntity<ResponseItemPageDto> getItemListByWanted(int page, int pageSize) {
        RequestSearchDto searchDto = new RequestSearchDto(page, pageSize);
        return ResponseEntity.ok(itemService.findAllByWanted(searchDto));
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
    public ResponseEntity<ResponseItemPageDto> getItemListByExchange(int page, int pageSize) {
        RequestSearchDto searchDto = new RequestSearchDto(page, pageSize);
        return ResponseEntity.ok(itemService.findAllByExchange(searchDto));
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
}