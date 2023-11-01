package R.VD.goomong.item.controller;

import R.VD.goomong.item.dto.request.RequestCategoryLv1;
import R.VD.goomong.item.dto.request.RequestCategoryLv2;
import R.VD.goomong.item.dto.response.ResponseItemCategoryDto;
import R.VD.goomong.item.service.ItemCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item-category")
public class ItemCategoryController {
    private final ItemCategoryService itemCategoryService;

    // 대분류를 위한 컨트롤러
    @PostMapping("/save/lv1")
    public ResponseEntity<String> createNewLv1Category(@Valid @RequestBody RequestCategoryLv1 requestCategory) {
        itemCategoryService.saveLevel1(requestCategory);
        return ResponseEntity.ok("카테고리 생성 완료");
    }


    // 소분류 저장을 위한 컨트롤러
    @PostMapping("/save/lv2")
    public ResponseEntity<String> createNewLv2Category(@Valid @RequestBody RequestCategoryLv2 requestCategory) {
        itemCategoryService.saveLevel2(requestCategory);
        return ResponseEntity.ok("카테고리 생성 완료");
    }

    @GetMapping("/list")
    public ResponseEntity<List<ResponseItemCategoryDto>> getCategoryList() {
        return ResponseEntity.ok(itemCategoryService.findAll());
    }

    @GetMapping("/list/lv1")
    public ResponseEntity<List<ResponseItemCategoryDto>> getCategoryLv1List() {
        return ResponseEntity.ok(itemCategoryService.findAllByLevelOne());
    }
}
