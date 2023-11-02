package R.VD.goomong.item.service;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.service.ImageService;
import R.VD.goomong.item.dto.request.RequestItemDto;
import R.VD.goomong.item.dto.request.RequestNonSaleItemDto;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.dto.response.ResponseNonSaleItemDto;
import R.VD.goomong.item.exception.NotFoundItem;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.model.ItemCategory;
import R.VD.goomong.item.model.Status;
import R.VD.goomong.item.repository.ItemCategoryRepository;
import R.VD.goomong.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    // 아이템 저장 - 판매
    public void save(RequestItemDto itemDto, MultipartFile[] multipartFiles) {
        saveItem(multipartFiles, itemDto.toEntity(), itemDto.getItemCategories());
    }

    // 아이템 저장 - 판매가 아닌 경우
    public void save(RequestNonSaleItemDto itemDto, MultipartFile[] multipartFiles) {
        saveItem(multipartFiles, itemDto.toEntity(), itemDto.getItemCategories());
    }

    // 아이템 찾기
    public ResponseItemDto findById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundItem("존재하지 않거나, 삭제된 아이템 입니다."));
        return new ResponseItemDto(item);
    }

    // 전체 아이템 찾기
    public List<ResponseItemDto> findAll() {
        List<Item> items = itemRepository.findAll();
        return items.stream().map(ResponseItemDto::new).toList();
    }

    // 판매 조회
    public List<ResponseItemDto> findAllBySale(Pageable pageable) {
        Page<Item> items = itemRepository.findAllByStatus(Status.SALE.toString(), pageable);
        List<ResponseItemDto> result = new ArrayList<>();
        for (Item item : items) {
            result.add(new ResponseItemDto(item));
        }

        return result;
    }

    // 재능 기부 조회
    public List<ResponseNonSaleItemDto> findAllByGive(Pageable pageable) {
        Page<Item> items = itemRepository.findAllByStatus(Status.GIVE.toString(), pageable);
        List<ResponseNonSaleItemDto> result = new ArrayList<>();
        for (Item item : items) {
            result.add(new ResponseNonSaleItemDto(item));
        }

        return result;
    }

    // 구인 조회
    public List<ResponseNonSaleItemDto> findAllByExchange(Pageable pageable) {
        Page<Item> items = itemRepository.findAllByStatus(Status.EXCHANGE.toString(), pageable);
        List<ResponseNonSaleItemDto> result = new ArrayList<>();
        for (Item item : items) {
            result.add(new ResponseNonSaleItemDto(item));
        }
        return result;
    }

    // 구인 조회
    public List<ResponseNonSaleItemDto> findAllByWanted(Pageable pageable) {
        Page<Item> items = itemRepository.findAllByStatus(Status.WANTED.toString(), pageable);
        List<ResponseNonSaleItemDto> result = new ArrayList<>();
        for (Item item : items) {
            result.add(new ResponseNonSaleItemDto(item));
        }

        return result;
    }

    public void deleteItem(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new NotFoundItem();
        }

        Item delItem = item.get();
        delItem.deleteItem();
    }

    // 공통 아이템 저장 함수
    private void saveItem(MultipartFile[] multipartFiles, Item entity, List<Long> itemCategories) {
        List<Image> imageList = imageService.saveImage(multipartFiles);
        List<ItemCategory> categories = new ArrayList<>();
        for (Long categoryId : itemCategories) {
            Optional<ItemCategory> findCategory = categoryRepository.findById(categoryId);
            findCategory.ifPresent(categories::add);
        }

        entity.setItemCategories(categories);
        entity.setThumbNailList(imageList);
        itemRepository.save(entity);
    }
}
