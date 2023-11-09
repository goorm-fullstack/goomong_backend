package R.VD.goomong.item.service;

import R.VD.goomong.item.dto.request.RequestCategoryLv1;
import R.VD.goomong.item.dto.request.RequestCategoryLv2;
import R.VD.goomong.item.dto.response.ResponseItemCategoryDto;
import R.VD.goomong.item.exception.NotFoundItemCategory;
import R.VD.goomong.item.model.ItemCategory;
import R.VD.goomong.item.repository.ItemCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public void deleteCategory(Long id) {
        Optional<ItemCategory> opt_category = itemCategoryRepository.findById(id);
        if (opt_category.isEmpty())
            throw new NotFoundItemCategory();

        ItemCategory category = opt_category.get();

        if (category.getLevel() == 1)//부모 카테고리인 경우
            deleteParentCategory(category);
        else //자식 카테고리인 경우
            deleteChildCategory(category);
    }

    // 부모 카테고리로부터 자식 카테고리 리스트를 받아옵니다.
    public List<ResponseItemCategoryDto> findLevelTwoByParentId(long id) {
        Optional<ItemCategory> category = itemCategoryRepository.findById(id);
        if (category.isEmpty())
            throw new NotFoundItemCategory();

        ItemCategory parentCategory = category.get();
        if (parentCategory.getLevel() != 1)
            throw new RuntimeException();

        List<ResponseItemCategoryDto> result = new ArrayList<>();
        for (ItemCategory categoryLv2 : parentCategory.getChildCategory()) {
            result.add(new ResponseItemCategoryDto(categoryLv2));
        }

        return result;
    }

    /**
     * 부모 카테고리 삭제
     * 먼저 자식 요소를 삭제한 후에 부모 요소를 삭제한다.
     */
    private void deleteParentCategory(ItemCategory itemCategory) {
        List<ItemCategory> childCategories = itemCategory.getChildCategory();
        for (ItemCategory category : childCategories) {
            itemCategoryRepository.deleteById(category.getId());
        }
        itemCategoryRepository.deleteById(itemCategory.getId());
    }

    /**
     * 자식 카테고리 삭제
     */
    private void deleteChildCategory(ItemCategory itemCategory) {
        itemCategoryRepository.deleteById(itemCategory.getId());
    }
}
