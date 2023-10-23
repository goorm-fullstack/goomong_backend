package R.VD.goomong.post.service;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.service.ImageService;
import R.VD.goomong.post.dto.request.RequestPostCategoryDto;
import R.VD.goomong.post.exception.NotExistPostCategoryException;
import R.VD.goomong.post.model.PostCategory;
import R.VD.goomong.post.repository.PostCategoryRepository;
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
public class PostCategoryService {

    private final PostCategoryRepository postCategoryRepository;
    private final ImageService imageService;

    // 게시글 카테고리 생성
    public Long savePostCategory(RequestPostCategoryDto requestPostCategoryDto, MultipartFile[] postCategoryImage) {
        PostCategory entity = requestPostCategoryDto.toEntity();

        List<Image> image = null;
        if (postCategoryImage.length != 0) image = imageService.saveImage(postCategoryImage);

        entity.toBuilder()
                .image(image)
                .build();
        return postCategoryRepository.save(entity).getId();
    }

    // 게시글 카테고리 수정
    public PostCategory updatePostCategory(Long postCategoryId, RequestPostCategoryDto requestPostCategoryDto, MultipartFile[] postCategoryImage) {
        PostCategory onePostCategory = findOnePostCategory(postCategoryId);

        List<Image> image = null;
        if (postCategoryImage.length != 0) image = imageService.saveImage(postCategoryImage);

        onePostCategory.toBuilder()
                .postCategoryName(requestPostCategoryDto.getPostCategoryName())
                .chgDate(LocalDateTime.now())
                .build();
        if (image != null) onePostCategory.toBuilder().image(image).build();

        postCategoryRepository.save(onePostCategory);
        return onePostCategory;
    }

    // 게시글 카테고리 소프트딜리트
    public void softDeletePostCategory(Long postCategoryId) {
        PostCategory onePostCategory = findOnePostCategory(postCategoryId);
        onePostCategory.toBuilder()
                .delDate(LocalDateTime.now())
                .build();
    }

    // 게시글 카테고리 완전 삭제
    public void deletePostCategory(Long postCategoryId) {
        PostCategory onePostCategory = findOnePostCategory(postCategoryId);
        postCategoryRepository.delete(onePostCategory);
    }

    // 하나의 게시글 카테고리 조회
    public PostCategory findOnePostCategory(Long postCategoryId) {
        return postCategoryRepository.findById(postCategoryId).orElseThrow(() -> new NotExistPostCategoryException("해당 카테고리를 찾을 수 없습니다."));
    }

    // 삭제되지 않은 게시글 카테고리 조회
    public List<PostCategory> listOfNotDeleted() {
        List<PostCategory> all = postCategoryRepository.findAll();
        List<PostCategory> list = new ArrayList<>();

        for (PostCategory postCategory : all) {
            if (postCategory.getDelDate() == null) list.add(postCategory);
        }
        return list;
    }

    // 삭제된 게시글 카테고리 조회
    public List<PostCategory> listOfDeleted() {
        List<PostCategory> all = postCategoryRepository.findAll();
        List<PostCategory> list = new ArrayList<>();

        for (PostCategory postCategory : all) {
            if (postCategory.getDelDate() != null) list.add(postCategory);
        }
        return list;
    }

    // 전체 게시글 카테고리 조회
    public List<PostCategory> allList() {
        return postCategoryRepository.findAll();
    }
}
