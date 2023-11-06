package R.VD.goomong.item.controller;

import R.VD.goomong.item.dto.request.RequestItemDto;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.service.ItemService;
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


@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/list")
    public ResponseEntity<List<ResponseItemDto>> getItemList(@RequestParam Optional<String> orderBy,
                                                             @RequestParam Optional<String> direction,
                                                             Pageable pageable) {
        Sort sort = Sort.unsorted();

        if (orderBy.isPresent() && direction.isPresent()) {
            Sort.Direction dir = Sort.Direction.fromString(direction.get());
            sort = Sort.by(dir, orderBy.get());
        }
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<ResponseItemDto> all = itemService.findAll(pageable);

        long totalElements = all.getTotalElements();
        int totalPages = all.getTotalPages();
        return ResponseEntity.ok()
                .header("TotalPages", String.valueOf(totalPages))
                .header("TotalData", String.valueOf(totalElements))
                .body(all.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseItemDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.findById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> writeItem(MultipartFile[] multipartFiles, @Valid @RequestPart RequestItemDto itemDto) {
        itemService.save(itemDto, multipartFiles);
        return ResponseEntity.ok("작성이 완료되었습니다.");
    }
}
