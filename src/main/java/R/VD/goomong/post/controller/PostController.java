package R.VD.goomong.post.controller;

import R.VD.goomong.post.dto.request.RequestPostDto;
import R.VD.goomong.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
     * @param itemImages     - 상품 이미지
     * @param postImages     - 게시글 이미지(상품 이미지와는 별개로)
     * @param postFiles      - 게시글 첨부파일
     * @param bindingResult  - validation
     * @return 성공적으로 작성 시 200
     */
    @PostMapping("/post")
    public ResponseEntity<Object> initPost(@Validated @RequestPart RequestPostDto requestPostDto, @RequestParam(required = false) MultipartFile[] itemImages, @RequestParam(required = false) MultipartFile[] postImages, @RequestParam(required = false) MultipartFile[] postFiles, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().build();

        postService.savePost(requestPostDto, itemImages, postImages, postFiles);
        return ResponseEntity.ok().build();
    }
}
