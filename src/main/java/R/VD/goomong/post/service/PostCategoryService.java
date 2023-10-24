package R.VD.goomong.post.service;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.service.ImageService;
import R.VD.goomong.post.dto.request.RequestPostCategoryDto;
import R.VD.goomong.post.exception.AlreadyDeletePostCategoryException;
import R.VD.goomong.post.exception.NotExistPostCategoryException;
import R.VD.goomong.post.exception.NotExistPostCategoryInfoException;
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
    public void savePostCategory(RequestPostCategoryDto requestPostCategoryDto, MultipartFile[] postCategoryImage) {

        if (postCategoryImage.length > 1) throw new NotExistPostCategoryInfoException("카테고리 이미지는 대표 이미지 1장만 가능합니다.");

        PostCategory entity = requestPostCategoryDto.toEntity();

        Image image = null;
        if (postCategoryImage.length != 0) {
            List<Image> images = imageService.saveImage(postCategoryImage);
            image = images.get(0);
        }

        PostCategory build = entity.toBuilder()
                .image(image)
                .build();
        postCategoryRepository.save(build);
    }

    // 게시글 카테고리 수정
    public PostCategory updatePostCategory(Long postCategoryId, RequestPostCategoryDto requestPostCategoryDto, MultipartFile[] postCategoryImage) {

        if (postCategoryImage.length > 1) throw new NotExistPostCategoryInfoException("카테고리 이미지는 대표 이미지 1장만 가능합니다.");

        PostCategory onePostCategory = findOnePostCategory(postCategoryId);

        Image image = null;
        if (postCategoryImage.length != 0) {
            List<Image> images = imageService.saveImage(postCategoryImage);
            image = images.get(0);
        }

        PostCategory build = onePostCategory.toBuilder()
                .postCategoryName(requestPostCategoryDto.getPostCategoryName())
                .build();
        if (image != null) build = build.toBuilder().image(image).build();

        postCategoryRepository.save(build);
        return build;
    }

    // 게시글 카테고리 소프트딜리트
    public void softDeletePostCategory(Long postCategoryId) {
        PostCategory onePostCategory = findOnePostCategory(postCategoryId);
        if (onePostCategory.getDelDate() != null) throw new AlreadyDeletePostCategoryException("이미 삭제된 카테고리입니다.");

        PostCategory build = onePostCategory.toBuilder()
                .delDate(LocalDateTime.now())
                .build();
        postCategoryRepository.save(build);
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
