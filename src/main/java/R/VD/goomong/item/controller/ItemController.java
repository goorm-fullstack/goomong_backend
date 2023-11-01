package R.VD.goomong.item.controller;

import R.VD.goomong.item.dto.request.RequestExchangeItemDto;
import R.VD.goomong.item.dto.request.RequestGiveItemDto;
import R.VD.goomong.item.dto.request.RequestItemDto;
import R.VD.goomong.item.dto.request.RequestWantedItemDto;
import R.VD.goomong.item.dto.response.ResponseExchangeItemDto;
import R.VD.goomong.item.dto.response.ResponseGiveItemDto;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.dto.response.ResponseWantedItemDto;
import R.VD.goomong.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    @PostMapping("/sale/save")
    public ResponseEntity<String> writeSaleItem(MultipartFile[] multipartFiles, @Valid @RequestPart RequestItemDto itemDto) {
        itemService.save(itemDto, multipartFiles);
        return ResponseEntity.ok("작성이 완료되었습니다.");
    }

    @PostMapping("/give/save")
    public ResponseEntity<String> writeGiveItem(MultipartFile[] multipartFiles, @Valid @RequestPart RequestGiveItemDto itemDto) {
        itemService.save(itemDto, multipartFiles);
        return ResponseEntity.ok("작성이 완료되었습니다.");
    }

    @PostMapping("/wanted/save")
    public ResponseEntity<String> writeWantedItem(MultipartFile[] multipartFiles, @Valid @RequestPart RequestWantedItemDto itemDto) {
        itemService.save(itemDto, multipartFiles);
        return ResponseEntity.ok("작성이 완료되었습니다.");
    }

    @PostMapping("/exchage/save")
    public ResponseEntity<String> writeExchangeItem(MultipartFile[] multipartFiles, @Valid @RequestPart RequestExchangeItemDto itemDto) {
        itemService.save(itemDto, multipartFiles);
        return ResponseEntity.ok("작성이 완료되었습니다.");
    }

    @GetMapping("/list/sale")
    public ResponseEntity<List<ResponseItemDto>> getItemListBySale(Pageable pageable) {
        return ResponseEntity.ok(itemService.findAllBySale(pageable));
    }

    @GetMapping("/list/give")
    public ResponseEntity<List<ResponseGiveItemDto>> getItemListByGive(Pageable pageable) {
        return ResponseEntity.ok(itemService.findAllByGive(pageable));
    }

    @GetMapping("/list/wanted")
    public ResponseEntity<List<ResponseWantedItemDto>> getItemListByWanted(Pageable pageable) {
        return ResponseEntity.ok(itemService.findAllByWanted(pageable));
    }

    @GetMapping("/list/exchange")
    public ResponseEntity<List<ResponseExchangeItemDto>> getItemListByExchange(Pageable pageable) {
        return ResponseEntity.ok(itemService.findAllByExchange(pageable));
    }
}
