package R.VD.goomong.post.controller;

import R.VD.goomong.post.dto.request.RequestPostDto;
import R.VD.goomong.post.dto.response.ResponsePostDto;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    /**
     * 게시글 생성
     *
     * @param requestPostDto - 게시글 생성 request
     * @param postImages     - 게시글 이미지(상품 이미지와는 별개로)
     * @param postFiles      - 게시글 첨부파일
     * @param bindingResult  - validation
     * @return 성공적으로 작성 시 200
     */
    @PostMapping("/post")
    public ResponseEntity<Object> initPost(@Validated @ModelAttribute RequestPostDto requestPostDto, @RequestParam(required = false) MultipartFile[] postImages, @RequestParam(required = false) MultipartFile[] postFiles, BindingResult bindingResult) {

        log.debug("requestPostDto={}", requestPostDto.toString());

        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().build();

        postService.savePost(requestPostDto, postImages, postFiles);
        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 수정
     *
     * @param postId         - 수정할 게시글의 pk
     * @param requestPostDto - 수정할 내용
     * @param postImages     - 수정할 이미지
     * @param postFiles      - 수정할 파일
     * @param bindingResult  - validation
     * @return 수정된 게시글
     */
    @PutMapping("/post/{postId}")
    public ResponseEntity<ResponsePostDto> modifyPost(@PathVariable Long postId, @Validated @ModelAttribute RequestPostDto requestPostDto, @RequestParam(required = false) MultipartFile[] postImages, @RequestParam(required = false) MultipartFile[] postFiles, BindingResult bindingResult) {

        log.info("postId={}", postId);
        log.debug("requestPostDto={}", requestPostDto.toString());

        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().build();

        ResponsePostDto responsePostDto = postService.updatePost(postId, requestPostDto, postImages, postFiles).toResponsePostDto();
        return ResponseEntity.ok(responsePostDto);
    }

    /**
     * 게시글 소프트딜리트
     *
     * @param postId - 삭제할 게시글 pk
     * @return 삭제 완료시 200
     */
    @PutMapping("/post/softdel/{postId}")
    public ResponseEntity<Object> softDeletePost(@PathVariable Long postId) {

        log.info("postId={}", postId);

        postService.softDeletePost(postId);
        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 완전 삭제
     *
     * @param postId - 삭제할 게시글 pk
     * @return - 삭제 완료시 200
     */
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Object> deletePost(@PathVariable Long postId) {

        log.info("postId={}", postId);

        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    /**
     * 클라이언트가 게시글 조회
     *
     * @param postId - 조회할 게시글 pk
     * @return - 조회된 게시글
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<ResponsePostDto> viewPost(@PathVariable Long postId) {

        log.info("postId={}", postId);

        postService.increaseViewCount(postId);
        Post onePost = postService.findOnePost(postId);
        return ResponseEntity.ok(onePost.toResponsePostDto());
    }

    /**
     * 조회수가 증가하지 않아야하는 경우의 게시글 조회(예: 관리자 페이지)
     *
     * @param postId - 조회할 게시글 pk
     * @return - 조회된 게시글
     */
    @GetMapping("/post/admin/{postId}")
    public ResponseEntity<ResponsePostDto> adminViewPost(@PathVariable Long postId) {

        log.info("postId={}", postId);

        ResponsePostDto responsePostDto = postService.findOnePost(postId).toResponsePostDto();
        return ResponseEntity.ok(responsePostDto);
    }

    /**
     * 좋아요 버튼 클릭
     *
     * @param postId - 좋아요 클릭 할 게시글 pk
     * @return - 해당 게시글의 좋아요 수
     */
    @GetMapping("/post/like/{postId}")
    public ResponseEntity<Integer> likePost(@PathVariable Long postId) {

        log.info("postId={}", postId);

        postService.increaseLikeCount(postId);
        int postLikeNo = postService.findOnePost(postId).toResponsePostDto().getPostLikeNo();
        return ResponseEntity.ok(postLikeNo);
    }

    /**
     * 삭제되지 않은 게시글 조회
     *
     * @param pageable - 페이징
     * @return - 조회된 게시글
     */
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    @GetMapping
    public ResponseEntity<List<ResponsePostDto>> listOfNotDeleted(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return getListResponseEntity(postService.listOfNotDeleted(pageable));
    }

    /**
     * 삭제된 게시글 조회
     *
     * @param pageable - 페이징
     * @return 조회된 게시글
     */
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    @GetMapping("/deleted")
    public ResponseEntity<List<ResponsePostDto>> listOfDeleted(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return getListResponseEntity(postService.listOfDeleted(pageable));
    }

    /**
     * 모든 게시글 조회
     *
     * @param pageable - 페이징
     * @return - 조회된 게시글
     */
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    @GetMapping("/all")
    public ResponseEntity<List<ResponsePostDto>> allList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return getListResponseEntity(postService.allList(pageable));
    }

    private ResponseEntity<List<ResponsePostDto>> getListResponseEntity(Page<Post> postService) {
        Page<Post> posts = postService;

        long totalElements = posts.getTotalElements();
        int totalPages = posts.getTotalPages();

        return ResponseEntity.ok()
                .header("TotalPages", String.valueOf(totalPages))
                .header("TotalData", String.valueOf(totalElements))
                .body(posts.getContent().stream().map(Post::toResponsePostDto).toList());
    }
}
