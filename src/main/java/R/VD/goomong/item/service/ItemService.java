package R.VD.goomong.item.service;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.service.ImageService;
import R.VD.goomong.item.dto.request.RequestItemDto;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.exception.NotFoundItem;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final ImageService imageService;

    // 아이템 저장
    public Item save(RequestItemDto itemDto, MultipartFile[] multipartFiles) {
        Item item = itemDto.toEntity();
        List<Image> imageList = imageService.saveImage(multipartFiles);
        item.setThumbNailList(imageList);
        return itemRepository.save(item);
    }

    // 아이템 찾기
    public ResponseItemDto findById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundItem("존재하지 않거나, 삭제된 아이템 입니다."));
        return new ResponseItemDto(item);
    }

    public List<ResponseItemDto> findAll() {
        List<Item> items = itemRepository.findAll();
        return items.stream().map(ResponseItemDto::new).toList();
    }
}
