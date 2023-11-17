package R.VD.goomong.post.service;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.service.ImageService;
import R.VD.goomong.post.dto.request.RequestCategoryDto;
import R.VD.goomong.post.exception.AlreadyDeleteCategoryException;
import R.VD.goomong.post.exception.NotDeletedCategoryException;
import R.VD.goomong.post.exception.NotExistCategoryException;
import R.VD.goomong.post.model.Category;
import R.VD.goomong.post.model.Type;
import R.VD.goomong.post.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

    // 게시글 카테고리 생성
    public void saveCategory(RequestCategoryDto requestCategoryDto, MultipartFile categoryImage) {

        Category entity = requestCategoryDto.toEntity();

        Type categoryGroup = Type.COMMUNITY;
        categoryGroup = categoryGroup.toType(requestCategoryDto.getCategoryGroup());

        Image image = null;
        if (categoryImage != null) {
            MultipartFile[] imageArray = new MultipartFile[1];
            imageArray[0] = categoryImage;
            List<Image> images = imageService.saveImage(imageArray);
            image = images.get(0);
        }

        Category build = entity.toBuilder()
                .categoryGroup(categoryGroup)
                .image(image)
                .build();
        categoryRepository.save(build);
    }

    // 게시글 카테고리 수정
    public Category updateCategory(Long categoryId, RequestCategoryDto requestCategoryDto, MultipartFile categoryImage) {

        Category onePostCategory = findOneCategory(categoryId);
        if (onePostCategory.getDelDate() != null)
            throw new AlreadyDeleteCategoryException("해당 id의 카테고리는 삭제된 카테고리입니다. id = " + categoryId);

        Type categoryGroup = Type.COMMUNITY;
        categoryGroup = categoryGroup.toType(requestCategoryDto.getCategoryGroup());

        Image image = null;
        if (categoryImage != null) {
            MultipartFile[] imageArray = new MultipartFile[1];
            imageArray[0] = categoryImage;
            List<Image> images = imageService.saveImage(imageArray);
            image = images.get(0);
        }

        Category build = onePostCategory.toBuilder()
                .categoryName(requestCategoryDto.getCategoryName())
                .categoryGroup(categoryGroup)
                .image(image)
                .build();

        return categoryRepository.save(build);
    }

    // 게시글 카테고리 소프트딜리트
    public void softDeleteCategory(Long categoryId) {
        Category oneCategory = findOneCategory(categoryId);
        if (oneCategory.getDelDate() != null)
            throw new AlreadyDeleteCategoryException("해당 id의 카테고리는 이미 삭제된 카테고리입니다. id = " + categoryId);

        Category build = oneCategory.toBuilder()
                .delDate(LocalDateTime.now())
                .build();
        categoryRepository.save(build);
    }

    // 게시글 카테고리 완전 삭제
    public void deleteCategory(Long categoryId) {
        Category oneCategory = findOneCategory(categoryId);
        if (oneCategory.getDelDate() != null)
            throw new AlreadyDeleteCategoryException("해당 id의 카테고리는 삭제된 카테고리입니다. id = " + categoryId);
        categoryRepository.delete(oneCategory);
    }

    // 삭제된 게시글 카테고리 복구
    public void unDelete(Long categoryId) {
        Category origin = categoryRepository.findById(categoryId).orElseThrow(() -> new NotExistCategoryException("해당 id의 카테고리는 없습니다. id = " + categoryId));
        if (origin.getDelDate() == null)
            throw new NotDeletedCategoryException("해당 id의 카테고리는 삭제되지 않은 카테고리입니다. id = " + categoryId);

        Category build = origin.toBuilder()
                .delDate(null)
                .build();
        categoryRepository.save(build);
    }

    // 하나의 게시글 카테고리 조회
    public Category findOneCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotExistCategoryException("해당 id의 카테고리를 찾을 수 없습니다. id = " + categoryId));
        if (category.getDelDate() != null)
            throw new AlreadyDeleteCategoryException("해당 id의 카테고리는 삭제된 카테고리입니다. id = " + categoryId);
        return category;
    }

    // 삭제되지 않은 카테고리 중 카테고리 이름으로 카테고리 리스트 조회
    public Page<Category> listOfNotDeletedAndName(String categoryName, Pageable pageable) {
        Page<Category> all = categoryRepository.findAll(pageable);
        List<Category> list = new ArrayList<>();

        for (Category category : all) {
            if (category.getCategoryName().equals(categoryName) && category.getDelDate() == null) list.add(category);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 삭제된 카테고리 중 카테고리 이름으로 카테고리 리스트 조회
    public Page<Category> listOfDeletedAndName(String categoryName, Pageable pageable) {
        Page<Category> all = categoryRepository.findAll(pageable);
        List<Category> list = new ArrayList<>();

        for (Category category : all) {
            if (category.getCategoryName().equals(categoryName) && category.getDelDate() != null) list.add(category);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 카테고리 이름으로 전체 카테고리 리스트 조회
    public Page<Category> listOfAllAndName(String categoryName, Pageable pageable) {
        Page<Category> all = categoryRepository.findAll(pageable);
        List<Category> list = new ArrayList<>();

        for (Category category : all) {
            if (category.getCategoryName().equals(categoryName)) list.add(category);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 삭제되지 않은 카테고리 조회
    public Page<Category> listOfNotDeleted(Pageable pageable) {
        Page<Category> all = categoryRepository.findAll(pageable);
        List<Category> list = new ArrayList<>();

        for (Category category : all) {
            if (category.getDelDate() == null) list.add(category);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 삭제된 게시글 카테고리 조회
    public Page<Category> listOfDeleted(Pageable pageable) {
        Page<Category> all = categoryRepository.findAll(pageable);
        List<Category> list = new ArrayList<>();

        for (Category category : all) {
            if (category.getDelDate() != null) list.add(category);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 전체 게시글 카테고리 조회
    public Page<Category> allList(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
}
