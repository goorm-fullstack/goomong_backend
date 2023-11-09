package R.VD.goomong.item.controller;

import R.VD.goomong.item.dto.request.RequestCategoryLv1;
import R.VD.goomong.item.dto.request.RequestCategoryLv2;
import R.VD.goomong.item.dto.response.ResponseItemCategoryDto;
import R.VD.goomong.item.service.ItemCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "itemCategory", description = "카테고리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item-category")
public class ItemCategoryController {
    private final ItemCategoryService itemCategoryService;

    // 대분류를 위한 컨트롤러
    @Operation(
            summary = "대분류 카테고리 추가",
            description = "대분류 카테고리를 추가합니다."
    )
    @PostMapping("/save/lv1")
    public ResponseEntity<String> createNewLv1Category(@Valid @RequestBody RequestCategoryLv1 requestCategory) {
        itemCategoryService.saveLevel1(requestCategory);
        return ResponseEntity.ok("카테고리 생성 완료");
    }


    // 소분류 저장을 위한 컨트롤러
    @Operation(
            summary = "소분류 카테고리 추가",
            description = "소분류 카테고리를 추가합니다."
    )
    @PostMapping("/save/lv2")
    public ResponseEntity<String> createNewLv2Category(@Valid @RequestBody RequestCategoryLv2 requestCategory) {
        itemCategoryService.saveLevel2(requestCategory);
        return ResponseEntity.ok("카테고리 생성 완료");
    }

    @Operation(
            summary = "카테고리 리스트 출력",
            description = "카테고리 리스트 출력.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "리스트 조회 성공",
                            content = @Content(schema = @Schema(implementation = ResponseItemCategoryDto.class)))
            }
    )
    @GetMapping("/list")
    public ResponseEntity<List<ResponseItemCategoryDto>> getCategoryList() {
        return ResponseEntity.ok(itemCategoryService.findAll());
    }

    @Operation(
            summary = "대분류 카테고리 리스트 출력",
            description = "대분류 카테고리 리스트 출력.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "리스트 조회 성공",
                            content = @Content(schema = @Schema(implementation = ResponseItemCategoryDto.class)))
            }
    )
    @GetMapping("/list/lv1")
    public ResponseEntity<List<ResponseItemCategoryDto>> getCategoryLv1List() {
        return ResponseEntity.ok(itemCategoryService.findAllByLevelOne());
    }

    @Operation(
            summary = "소분로 카테고리 리스트 출력",
            description = "대분류 카테고리 아이디로 소분류 카테고리 리스트를 출력합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "리스트 조회 성공",
                            content = @Content(schema = @Schema(implementation = ResponseItemCategoryDto.class)))
            }
    )
    @GetMapping("/list/lv2")
    public ResponseEntity<List<ResponseItemCategoryDto>> getCategoryLv2ByParentCategory(@RequestParam long id) {
        return ResponseEntity.ok(itemCategoryService.findLevelTwoByParentId(id));
    }
}
