package R.VD.goomong.post.controller;

import R.VD.goomong.post.dto.response.ResponseFaqCommunityPostDto;
import R.VD.goomong.post.dto.response.ResponseItemPostDto;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.post.service.PostService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/posts")
@Tag(name = "게시글(판매/기부/교환/커뮤니티/FAQ) 공통 api")
public class PostController {

    private final PostService postService;

    private static ResponseEntity<List<Object>> getListResponseEntity(Page<Post> posts) {
        List<Object> list = new ArrayList<>();

        for (Post post : posts) {
            if (post.getItem() != null) list.add(post.toResponseItemPostDto());
            else list.add(post.toResponseFaqCoummunityDto());
        }

        long totalElements = posts.getTotalElements();
        int totalPages = posts.getTotalPages();
        return ResponseEntity.ok()
                .header("TotalPages", String.valueOf(totalPages))
                .header("TotalData", String.valueOf(totalElements))
                .body(list);
    }

    /**
     * 게시글 소프트딜리트
     *
     * @param postId 삭제할 게시글 pk
     * @return 삭제 완료시 200
     */
    @Operation(summary = "게시글 삭제")
    @Parameter(name = "postId", description = "삭제할 게시글 id")
    @ApiResponse(responseCode = "200", description = "성공")
    @DeleteMapping("/post/softdel/{postId}")
    public ResponseEntity<Object> softDeletePost(@PathVariable Long postId) {

        log.info("postId={}", postId);

        postService.softDeletePost(postId);
        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 완전 삭제
     *
     * @param postId 삭제할 게시글 pk
     * @return 삭제 완료시 200
     */
    @Hidden
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Object> deletePost(@PathVariable Long postId) {

        log.info("postId={}", postId);

        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    /**
     * 삭제된 게시글 복구
     *
     * @param postId 복구할 게시글 pk
     * @return 복구 완료 시 200
     */
    @Operation(summary = "삭제된 게시글 복구")
    @Parameter(name = "postId", description = "복구할 게시글 id")
    @ApiResponse(responseCode = "200", description = "성공")
    @PutMapping("/post/undel/{postId}")
    public ResponseEntity<Object> unDeletedPost(@PathVariable Long postId) {
        log.info("postId={}", postId);
        postService.unDeleted(postId);
        return ResponseEntity.ok().build();
    }

    /**
     * 좋아요 버튼 클릭
     *
     * @param postId 좋아요 클릭 할 게시글 pk
     * @return 정상 작동 시 200
     */
    @Operation(summary = "좋아요 버튼 클릭")
    @Parameter(name = "postId", description = "좋아요 클릭할 게시글 id")
    @ApiResponse(responseCode = "200", description = "성공")
    @PutMapping("/post/like/{postId}")
    public ResponseEntity<Object> likePost(@PathVariable Long postId) {

        log.info("postId={}", postId);

        postService.increaseLikeCount(postId);
        return ResponseEntity.ok().build();
    }

    /**
     * 클라이언트가 게시글 조회
     *
     * @param postId 조회할 게시글 pk
     * @return 조회된 게시글
     */
    @Operation(summary = "클라이언트가 게시글 조회하는 경우(즉, 조회수 증가)")
    @Parameter(name = "postId", description = "조회할 게시글 id")
    @ApiResponse(responseCode = "200", description = "성공, 판매/기부/교환 게시글인 경우", content = @Content(schema = @Schema(implementation = ResponseItemPostDto.class)))
    @GetMapping("/post/{postId}")
    public ResponseEntity<Object> viewPost(@PathVariable Long postId) {

        log.info("postId={}", postId);

        postService.increaseViewCount(postId);

        return getResponseEntity(postId);
    }

    /**
     * 조회수가 증가하지 않아야하는 경우의 게시글 조회(예: 관리자 페이지)
     *
     * @param postId 조회할 게시글 pk
     * @return 조회된 게시글
     */
    @Operation(summary = "관리자 페이지에서의 게시글 조회")
    @Parameter(name = "postId", description = "조회할 게시글 id")
    @ApiResponse(responseCode = "200", description = "성공, 판매/기부/교환 게시글인 경우", content = @Content(schema = @Schema(implementation = ResponseItemPostDto.class)))
    @GetMapping("/post/admin/{postId}")
    public ResponseEntity<Object> adminViewPost(@PathVariable Long postId) {

        log.info("postId={}", postId);

        return getResponseEntity(postId);
    }

    /**
     * 게시글 종류에 따라 리스트 조회(판매/기부/교환/FAQ/커뮤니티)
     *
     * @param type     게시글 종류
     * @param pageable 페이징
     * @return 조회된 게시글 리스트
     */
    @Operation(summary = "게시글 종류에 따라 리스트 조회(판매/기부/교환/FAQ/커뮤니티)")
    @Parameters(value = {
            @Parameter(name = "type", description = "종류(판매/기부/교환/FAQ/커뮤니티)"),
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int"))
    })
    @ApiResponse(responseCode = "200", description = "성공, 판매/기부/교환 게시글인 경우", content = @Content(schema = @Schema(implementation = ResponseItemPostDto.class)))
    @GetMapping("/{type}")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<Object>> listOfType(@PathVariable String type, Pageable pageable) {
        log.info("type={}", type);
        Page<Post> posts = postService.listOfType(type, pageable);
        return getListResponseEntity(posts);
    }

    /**
     * 카테고리에 따라 게시글 리스트 조회
     *
     * @param category 게시글 카테고리
     * @param pageable 페이징
     * @return 조회된 게시글 리스트
     */
    @Operation(summary = "카테고리에 따라 게시글 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "category", description = "카테고리"),
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int"))
    })
    @ApiResponse(responseCode = "200", description = "성공, FAQ/커뮤니티인 경우", content = @Content(schema = @Schema(implementation = ResponseFaqCommunityPostDto.class)))
    @GetMapping("/{category}")
    @CrossOrigin(exposedHeaders = {"Totalpages", "TotalData"})
    public ResponseEntity<List<Object>> listOfCategory(@PathVariable String category, Pageable pageable) {
        log.info("category={}", category);
        Page<Post> posts = postService.listOfCategory(category, pageable);
        return getListResponseEntity(posts);
    }

    /**
     * 삭제되지 않은 게시글 조회
     *
     * @param pageable 페이징
     * @return 조회된 게시글
     */
    @Operation(summary = "삭제되지 않은 게시글 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int"))
    })
    @ApiResponse(responseCode = "200", description = "성공, 판매/기부/교환 게시글인 경우", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseItemPostDto.class))))
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    @GetMapping
    public ResponseEntity<List<Object>> listOfNotDeleted(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> posts = postService.listOfNotDeleted(pageable);
        return getListResponseEntity(posts);
    }

    /**
     * 삭제된 게시글 조회
     *
     * @param pageable 페이징
     * @return 조회된 게시글
     */
    @Operation(summary = "삭제된 게시글 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int"))
    })
    @ApiResponse(responseCode = "200", description = "성공, 판매/기부/교환 게시글인 경우", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseItemPostDto.class))))
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    @GetMapping("/deleted")
    public ResponseEntity<List<Object>> listOfDeleted(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> posts = postService.listOfDeleted(pageable);
        return getListResponseEntity(posts);
    }

    /**
     * 모든 게시글 조회
     *
     * @param pageable 페이징
     * @return 조회된 게시글
     */
    @Operation(summary = "모든 게시글 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int"))
    })
    @ApiResponse(responseCode = "200", description = "성공, 판매/기부/교환 게시글인 경우", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseItemPostDto.class))))
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    @GetMapping("/all")
    public ResponseEntity<List<Object>> allList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> posts = postService.allList(pageable);
        return getListResponseEntity(posts);
    }

    private ResponseEntity<Object> getResponseEntity(Long postId) {
        Post findPost = postService.findOnePost(postId);
        if (findPost.getItem() != null) return ResponseEntity.ok(findPost.toResponseItemPostDto());
        return ResponseEntity.ok(findPost.toResponseFaqCoummunityDto());
    }
}
