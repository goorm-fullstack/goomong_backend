package R.VD.goomong.item.controller;


import R.VD.goomong.item.dto.request.RequestItemDto;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/list")
    public ResponseEntity<List<ResponseItemDto>> getItemList() {
        return ResponseEntity.ok(itemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseItemDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.findById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> writeItem(MultipartFile[] multipartFiles, @RequestPart RequestItemDto itemDto) {
        itemService.save(itemDto, multipartFiles);
        return ResponseEntity.ok("작성이 완료되었습니다.");
    }
}
