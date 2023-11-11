package R.VD.goomong.post.service;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.service.ImageService;
import R.VD.goomong.post.dto.request.RequestCategoryDto;
import R.VD.goomong.post.exception.AlreadyDeleteCategoryException;
import R.VD.goomong.post.exception.NotDeletedCategoryException;
import R.VD.goomong.post.exception.NotExistCategoryException;
import R.VD.goomong.post.exception.NotExistCategoryInfoException;
import R.VD.goomong.post.model.Category;
import R.VD.goomong.post.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public void saveCategory(RequestCategoryDto requestCategoryDto, MultipartFile[] categoryImage) {

        if (categoryImage.length > 1) throw new NotExistCategoryInfoException("카테고리 이미지는 대표 이미지 1장만 가능합니다.");

        Category entity = requestCategoryDto.toEntity();

        Image image = null;
        if (categoryImage.length != 0) {
            List<Image> images = imageService.saveImage(categoryImage);
            image = images.get(0);
        }

        Category build = entity.toBuilder()
                .image(image)
                .build();
        categoryRepository.save(build);
    }

    // 게시글 카테고리 수정
    public Category updateCategory(Long categoryId, RequestCategoryDto requestCategoryDto, MultipartFile[] categoryImage) {

        if (categoryImage.length > 1) throw new NotExistCategoryInfoException("카테고리 이미지는 대표 이미지 1장만 가능합니다.");

        Category onePostCategory = findOneCategory(categoryId);
        if (onePostCategory.getDelDate() != null)
            throw new AlreadyDeleteCategoryException("해당 id의 카테고리는 삭제된 카테고리입니다. id = " + categoryId);

        Image image = null;
        if (categoryImage.length != 0) {
            List<Image> images = imageService.saveImage(categoryImage);
            image = images.get(0);
        }

        Category build = onePostCategory.toBuilder()
                .categoryName(requestCategoryDto.getCategoryName())
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

    // 삭제되지 않은 카테고리 조회
    public List<Category> listOfNotDeleted() {
        List<Category> all = categoryRepository.findAll();
        List<Category> list = new ArrayList<>();

        for (Category category : all) {
            if (category.getDelDate() == null) list.add(category);
        }
        return list;
    }

    // 삭제된 게시글 카테고리 조회
    public List<Category> listOfDeleted() {
        List<Category> all = categoryRepository.findAll();
        List<Category> list = new ArrayList<>();

        for (Category category : all) {
            if (category.getDelDate() != null) list.add(category);
        }
        return list;
    }

    // 전체 게시글 카테고리 조회
    public List<Category> allList() {
        return categoryRepository.findAll();
    }
}
