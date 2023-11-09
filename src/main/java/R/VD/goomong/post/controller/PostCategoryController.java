package R.VD.goomong.post.controller;

import R.VD.goomong.post.dto.request.RequestPostCategoryDto;
import R.VD.goomong.post.dto.response.ResponsePostCategoryDto;
import R.VD.goomong.post.model.PostCategory;
import R.VD.goomong.post.service.PostCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/postcategorys")
@Slf4j
public class PostCategoryController {

    private final PostCategoryService postCategoryService;

    /**
     * 카테고리 생성
     *
     * @param requestPostCategoryDto 카테고리 생성 request
     * @param postCategoryImages     카테고리 이미지
     * @return 생성 완료 시 200
     */
    @PostMapping("/postcategory")
    public ResponseEntity<Object> initPostCategory(@Validated @ModelAttribute RequestPostCategoryDto requestPostCategoryDto, @RequestParam(required = false) MultipartFile[] postCategoryImages) {
        log.info("requestPostCategoryDto={}", requestPostCategoryDto);
        postCategoryService.savePostCategory(requestPostCategoryDto, postCategoryImages);
        return ResponseEntity.ok().build();
    }

    /**
     * 카테고리 수정
     *
     * @param postCategoryId         수정할 카테고리 pk
     * @param requestPostCategoryDto 수정할 내용
     * @param postCategoryImages     수정할 이미지
     * @return 수정된 카테고리
     */
    @PutMapping("/postcategory/{postCategoryId}")
    public ResponseEntity<ResponsePostCategoryDto> modifyPostCategory(@PathVariable Long postCategoryId, @Validated @RequestPart RequestPostCategoryDto requestPostCategoryDto, @RequestParam(required = false) MultipartFile[] postCategoryImages) {
        log.info("postCategoryId={}", postCategoryId);
        log.info("requestPostCategoryDto={}", requestPostCategoryDto);
        PostCategory postCategory = postCategoryService.updatePostCategory(postCategoryId, requestPostCategoryDto, postCategoryImages);
        return ResponseEntity.ok(postCategory.toResponsePostCategoryDto());
    }

    /**
     * 카테고리 소프트딜리트
     *
     * @param postCategoryId 삭제할 카테고리 pk
     * @return 삭제 완료 시 200
     */
    @PutMapping("/postcategory/softdel/{postCategoryId}")
    public ResponseEntity<Object> softDeletePostCategory(@PathVariable Long postCategoryId) {
        log.info("postCategoryId={}", postCategoryId);
        postCategoryService.softDeletePostCategory(postCategoryId);
        return ResponseEntity.ok().build();
    }

    /**
     * 카테고리 완전 삭제
     *
     * @param postCategoryId 삭제할 카테고리 pk
     * @return 삭제 완료 시 200
     */
    @DeleteMapping("/postcategory/{postCategoryId}")
    public ResponseEntity<Object> deletePostCategory(@PathVariable Long postCategoryId) {
        log.info("postCategoryId={}", postCategoryId);
        postCategoryService.deletePostCategory(postCategoryId);
        return ResponseEntity.ok().build();
    }

    /**
     * 삭제된 카테고리 복구
     *
     * @param postCategoryId 삭제할 카테고리 id
     * @return 복구 완료 시 200
     */
    @PutMapping("/postcategory/undel/{postCategoryId}")
    public ResponseEntity<Object> unDelete(@PathVariable Long postCategoryId) {
        log.info("postCategoryId={}", postCategoryId);
        postCategoryService.unDelete(postCategoryId);
        return ResponseEntity.ok().build();
    }

    /**
     * 카테고리 조회
     *
     * @param postCategoryId 조회할 카테고리 pk
     * @return 조회된 카테고리
     */
    @GetMapping("/{postCategoryId}")
    public ResponseEntity<ResponsePostCategoryDto> viewPostCategory(@PathVariable Long postCategoryId) {
        log.info("postCategoryId={}", postCategoryId);
        PostCategory onePostCategory = postCategoryService.findOnePostCategory(postCategoryId);
        return ResponseEntity.ok(onePostCategory.toResponsePostCategoryDto());
    }

    /**
     * 삭제되지 않은 카테고리 조회
     *
     * @return 조회된 카테고리
     */
    @GetMapping()
    public ResponseEntity<List<ResponsePostCategoryDto>> listOfNotDeleted() {
        List<PostCategory> postCategories = postCategoryService.listOfNotDeleted();
        return ResponseEntity.ok(postCategories.stream().map(PostCategory::toResponsePostCategoryDto).toList());
    }

    /**
     * 삭제된 카테고리 조회
     *
     * @return 조회된 카테고리
     */
    @GetMapping("/deleted")
    public ResponseEntity<List<ResponsePostCategoryDto>> listOfDeleted() {
        List<PostCategory> postCategories = postCategoryService.listOfDeleted();
        return ResponseEntity.ok(postCategories.stream().map(PostCategory::toResponsePostCategoryDto).toList());
    }

    /**
     * 모든 카테고리 조회
     *
     * @return 조회된 카테고리
     */
    @GetMapping("/all")
    public ResponseEntity<List<ResponsePostCategoryDto>> allList() {
        List<PostCategory> postCategories = postCategoryService.allList();
        return ResponseEntity.ok(postCategories.stream().map(PostCategory::toResponsePostCategoryDto).toList());
    }
}
