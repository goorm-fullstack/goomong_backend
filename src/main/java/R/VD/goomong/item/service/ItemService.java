package R.VD.goomong.item.service;

import R.VD.goomong.global.model.PageInfo;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.service.ImageService;
import R.VD.goomong.item.dto.request.RequestItemDto;
import R.VD.goomong.item.dto.request.RequestSearchDto;
import R.VD.goomong.item.dto.request.UpdateItemDto;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.dto.response.ResponseItemPageDto;
import R.VD.goomong.item.dto.response.ResponseNonSaleItemDto;
import R.VD.goomong.item.exception.NotFoundItem;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.model.ItemCategory;
import R.VD.goomong.item.model.ItemOption;
import R.VD.goomong.item.model.Status;
import R.VD.goomong.item.repository.ItemCategoryRepository;
import R.VD.goomong.item.repository.ItemOptionRepository;
import R.VD.goomong.item.repository.ItemRepository;
import R.VD.goomong.member.exception.NotFoundMember;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final MemberRepository memberRepository;
    private final ItemOptionRepository itemOptionRepository;

    // 판매 상태의 값을 페이징처리해서 반환하는 함수
    private static ResponseItemPageDto<List<ResponseItemDto>> getListResponseItemPageDto(int page, int pageSize, Page<Item> items) {
        List<ResponseItemDto> result = new ArrayList<>();
        PageInfo pageInfo = PageInfo.builder()
                .page(page)
                .size(pageSize)
                .totalElements(items.getTotalElements())
                .totalPage(items.getTotalPages())
                .build();

        List<Item> itemList = items.getContent();
        for (Item item : itemList) {
            result.add(new ResponseItemDto(item));
        }

        return new ResponseItemPageDto<>(result, pageInfo);
    }

    // 아이템 저장
    public void save(RequestItemDto itemDto, MultipartFile[] multipartFiles) {
        saveItem(multipartFiles, itemDto, itemDto.getItemCategories());
    }

    // 아이템 찾기
    public ResponseItemDto findById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundItem("존재하지 않거나, 삭제된 아이템 입니다."));
        return new ResponseItemDto(item);
    }

    // 재능 기부 조회

    // 전체 아이템 찾기
    public ResponseItemPageDto<List<ResponseItemDto>> findAll(RequestSearchDto searchDto) {
        int page = searchDto.getPage() - 1;
        int pageSize = searchDto.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Item> items = itemRepository.findAll(pageable);

        PageInfo pageInfo = PageInfo.builder()
                .page(page)
                .size(pageSize)
                .totalElements(items.getTotalElements())
                .totalPage(items.getTotalPages())
                .build();


        List<Item> itemList = items.getContent();
        return new ResponseItemPageDto<>(itemList.stream().map(ResponseItemDto::new).toList(), pageInfo);
    }

    // 판매 조회
    public ResponseItemPageDto<List<ResponseItemDto>> findAllBySale(RequestSearchDto searchDto) {
        int page = searchDto.getPage() - 1;
        int pageSize = searchDto.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Item> items = itemRepository.findAllByStatus(Status.SALE.toString(), pageable);

        return getListResponseItemPageDto(page, pageSize, items);
    }

    public ResponseItemPageDto<List<ResponseNonSaleItemDto>> findAllByGive(RequestSearchDto searchDto) {
        return getListResponseItemPageDto(searchDto, Status.GIVE.toString());
    }

    public ResponseItemPageDto<List<ResponseNonSaleItemDto>> findAllByExchange(RequestSearchDto searchDto) {
        return getListResponseItemPageDto(searchDto, Status.EXCHANGE.toString());
    }

    public ResponseItemPageDto<List<ResponseNonSaleItemDto>> findAllByWanted(RequestSearchDto searchDto) {
        return getListResponseItemPageDto(searchDto, Status.WANTED.toString());
    }

    public void deleteItem(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new NotFoundItem();
        }

        Item delItem = item.get();
        delItem.deleteItem();
    }

    public void updateItem(UpdateItemDto itemDto) {
        Optional<Item> findItem = itemRepository.findById(itemDto.getId());
        if (findItem.isEmpty())
            throw new NotFoundItem();

        Item item = findItem.get();
        propertyUpdate(item, itemDto);
    }

    private void saveItem(MultipartFile[] multipartFiles, RequestItemDto entity, List<Long> itemCategories) {
        Item item = entity.toEntity();
        List<Image> imageList = imageService.saveImage(multipartFiles);
        List<ItemCategory> categories = new ArrayList<>();
        for (Long categoryId : itemCategories) {
            Optional<ItemCategory> findCategory = categoryRepository.findById(categoryId);
            findCategory.ifPresent(categories::add);
        }

        Optional<Member> member = memberRepository.findById(entity.getMemberId());
        if (member.isEmpty())
            throw new NotFoundMember();

        List<ItemOption> options = new ArrayList<>();
        for (ItemOption option : entity.getItemOptions()) {
            ItemOption save = itemOptionRepository.save(option);
            options.add(save);
        }

        item.setMember(member.get());
        item.setItemCategories(categories);
        item.setThumbNailList(imageList);
        item.setItemOptions(options);
        itemRepository.save(item);
    }

    /**
     * 아이템의 데이터를 업데이트 합니다.
     */
    private void propertyUpdate(Item item, UpdateItemDto itemDto) {
        String title = item.getTitle();
        String describe = item.getDescription();
        int price = item.getPrice();

        if (!itemDto.getDescribe().isEmpty())
            describe = itemDto.getDescribe();

        if (!itemDto.getTitle().isEmpty())
            title = itemDto.getTitle();

        if (itemDto.getPrice() > 0)
            price = itemDto.getPrice();

        item.update(price, title, describe);
    }

    private ResponseItemPageDto<List<ResponseNonSaleItemDto>> getListResponseItemPageDto(RequestSearchDto searchDto, String status) {
        int page = searchDto.getPage() - 1;
        int pageSize = searchDto.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Item> items = itemRepository.findAllByStatus(status, pageable);
        List<ResponseNonSaleItemDto> result = new ArrayList<>();
        PageInfo pageInfo = PageInfo.builder()
                .page(page)
                .size(pageSize)
                .totalElements(items.getTotalElements())
                .totalPage(items.getTotalPages())
                .build();
        List<Item> itemList = items.getContent();
        for (Item item : itemList) {
            result.add(new ResponseNonSaleItemDto(item));
        }

        return new ResponseItemPageDto<>(result, pageInfo);
    }
}