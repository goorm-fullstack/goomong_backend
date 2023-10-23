package goomong.item.service;

import goomong.item.dto.request.RequestCategoryLv1;
import goomong.item.dto.request.RequestCategoryLv2;
import goomong.item.dto.response.ResponseItemCategoryDto;
import goomong.item.exception.NotFoundItemCategory;
import goomong.item.model.ItemCategory;
import goomong.item.repository.ItemCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemCategoryService {
    private final ItemCategoryRepository itemCategoryRepository;

    // 대분류 저장
    public void saveLevel1(RequestCategoryLv1 requestCategory) {
        itemCategoryRepository.save(requestCategory.toEntity());
    }

    // 소분류 저장
    public void saveLevel2(RequestCategoryLv2 requestCategory) {
        Long parentId = requestCategory.getParentId();
        ItemCategory parentCategory = itemCategoryRepository.
                findById(parentId).orElseThrow(
                        () -> new NotFoundItemCategory("존재하지 않는 부모 카테고리입니다.")
                );

        ItemCategory childCategory = requestCategory.toEntity();
        childCategory.setParent(parentCategory);
        ItemCategory save = itemCategoryRepository.save(childCategory);
        parentCategory.getChildCategory().add(save);
    }

    public List<ResponseItemCategoryDto> findAll() {
        return itemCategoryRepository.findAll().stream().map(ResponseItemCategoryDto::new).toList();
    }

    public List<ResponseItemCategoryDto> findAllByLevelOne() {
        return itemCategoryRepository.findAllByLevel(1).stream().map(ResponseItemCategoryDto::new).toList();
    }
}
