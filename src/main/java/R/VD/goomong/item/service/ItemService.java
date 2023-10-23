package goomong.item.service;

import goomong.image.model.Image;
import goomong.image.service.ImageService;
import goomong.item.dto.request.RequestItemDto;
import goomong.item.dto.response.ResponseItemDto;
import goomong.item.exception.NotFoundItem;
import goomong.item.model.Item;
import goomong.item.model.ItemCategory;
import goomong.item.repository.ItemCategoryRepository;
import goomong.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final ImageService imageService;
    private final ItemCategoryRepository categoryRepository;

    // 아이템 저장
    public void save(RequestItemDto itemDto, MultipartFile[] multipartFiles) {
        Item item = itemDto.toEntity();
        List<Image> imageList = imageService.saveImage(multipartFiles);
        List<ItemCategory> categories = new ArrayList<>();
        for (Long categoryId : itemDto.getItemCategories()) {
            Optional<ItemCategory> findCategory = categoryRepository.findById(categoryId);
            findCategory.ifPresent(categories::add);
        }

        item.setItemCategories(categories);
        item.setThumbNailList(imageList);
        itemRepository.save(item);
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
