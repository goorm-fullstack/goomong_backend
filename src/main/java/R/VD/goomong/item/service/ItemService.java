package R.VD.goomong.item.service;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.service.ImageService;
import R.VD.goomong.item.dto.request.RequestItemDto;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.exception.NotFoundItem;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.model.ItemCategory;
import R.VD.goomong.item.repository.ItemCategoryRepository;
import R.VD.goomong.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public Page<ResponseItemDto> findAll(Pageable pageable) {
        Page<Item> all = itemRepository.findAll(pageable);
        List<Item> itemList = all.getContent();
        List<ResponseItemDto> list = itemList.stream().map(ResponseItemDto::new).toList();
        return new PageImpl<>(list, pageable, list.size());
    }

    public void deleteItem(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new NotFoundItem();
        }

        Item delItem = item.get();
        delItem.deleteItem();
    }
}
