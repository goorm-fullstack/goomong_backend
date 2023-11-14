package R.VD.goomong.post.controller;

import R.VD.goomong.global.validation.EnumValue;
import R.VD.goomong.post.dto.request.RequestPostDto;
import R.VD.goomong.post.dto.response.ResponsePostDto;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.post.model.Type;
import R.VD.goomong.post.service.PostService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/posts")
@Tag(name = "게시글 api")
public class PostController {

    private final PostService postService;

    private static ResponseEntity<List<ResponsePostDto>> getListResponseEntity(Page<Post> posts) {
        List<ResponsePostDto> list = new ArrayList<>();

        for (Post post : posts) {
            list.add(post.toResponsePostDto());
        }

        long totalElements = posts.getTotalElements();
        int totalPages = posts.getTotalPages();
        return ResponseEntity.ok()
                .header("TotalPages", String.valueOf(totalPages))
                .header("TotalData", String.valueOf(totalElements))
                .body(list);
    }

    /**
     * 게시글 생성
     *
     * @param requestPostDto 생성 request
     * @param images         업로드한 이미지 리스트
     * @param files          업로드한 파일 리스트
     * @return 생성 성공 시 200
     */
    @Operation(summary = "게시글 생성")
    @Parameters(value = {
            @Parameter(name = "images", description = "업로드한 이미지 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile"))),
            @Parameter(name = "files", description = "업로드한 파일 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile")))
    })
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> initPost(@Validated @ModelAttribute RequestPostDto requestPostDto, MultipartFile[] images, MultipartFile[] files) {
        log.info("requestPostDto={}", requestPostDto);

        postService.savePost(requestPostDto, images, files);
        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 수정
     *
     * @param postId         수정할 게시글 pk
     * @param requestPostDto 수정 내용
     * @param images         수정할 이미지 리스트
     * @param files          수정할 파일 리스트
     * @return 수정된 게시글
     */
    @Operation(summary = "게시글 수정")
    @Parameters(value = {
            @Parameter(name = "postId", description = "수정할 게시글 id"),
            @Parameter(name = "images", description = "수정할 이미지 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile"))),
            @Parameter(name = "files", description = "수정할 파일 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile")))
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponsePostDto.class)))
    @PutMapping(value = "/post/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponsePostDto> updatePost(@PathVariable Long postId, @Validated @ModelAttribute RequestPostDto requestPostDto, MultipartFile[] images, MultipartFile[] files) {
        log.info("postId={}", postId);
        log.info("requestPostDto={}", requestPostDto);

        ResponsePostDto responsePostDto = postService.updatePost(postId, requestPostDto, images, files).toResponsePostDto();
        return ResponseEntity.ok(responsePostDto);
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
     * 클라이언트가 게시글 조회
     *
     * @param postId 조회할 게시글 pk
     * @return 조회된 게시글
     */
    @Operation(summary = "클라이언트가 게시글 조회하는 경우(즉, 조회수 증가)")
    @Parameter(name = "postId", description = "조회할 게시글 id")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponsePostDto.class)))
    @GetMapping("/post/{postId}")
    public ResponseEntity<ResponsePostDto> viewPost(@PathVariable Long postId) {

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
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponsePostDto.class)))
    @GetMapping("/post/admin/{postId}")
    public ResponseEntity<ResponsePostDto> adminViewPost(@PathVariable Long postId) {

        log.info("postId={}", postId);

        return getResponseEntity(postId);
    }

    /**
     * 삭제되지 않은 게시글 중 게시글 종류에 따라 리스트 조회(커뮤니티/공지사항/이벤트)
     *
     * @param type     게시글 종류
     * @param pageable 페이징
     * @return 조회된 게시글 리스트
     */
    @Operation(summary = "삭제되지 않은 게시글 중 게시글 종류에 따라 리스트 조회(커뮤니티/공지사항/이벤트)")
    @Parameters(value = {
            @Parameter(name = "type", description = "종류(커뮤니티/공지사항/이벤트)", schema = @Schema(implementation = Type.class)),
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))), headers = {
            @Header(name = "TotalPages", description = "전체 페이지 개수", schema = @Schema(type = "string")),
            @Header(name = "TotalData", description = "전체 데이터 개수", schema = @Schema(type = "string"))
    })
    @GetMapping("/notdeletedtype/{type}")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponsePostDto>> listOfNotDeletedAndType(@EnumValue(enumClass = Type.class) @PathVariable String type, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("type={}", type);
        Page<Post> posts = postService.listOfNotDeletedAndType(type, pageable);
        return getListResponseEntity(posts);
    }

    /**
     * 삭제된 게시글 중 게시글 종류에 따라 리스트 조회(커뮤니티/공지사항/이벤트)
     *
     * @param type     게시글 종류
     * @param pageable 페이징
     * @return 조회된 게시글 리스트
     */
    @Operation(summary = "삭제된 게시글 중 게시글 종류에 따라 리스트 조회(커뮤니티/공지사항/이벤트)")
    @Parameters(value = {
            @Parameter(name = "type", description = "종류(커뮤니티/공지사항/이벤트)", schema = @Schema(implementation = Type.class)),
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))), headers = {
            @Header(name = "TotalPages", description = "전체 페이지 개수", schema = @Schema(type = "string")),
            @Header(name = "TotalData", description = "전체 데이터 개수", schema = @Schema(type = "string"))
    })
    @GetMapping("/deletedtype/{type}")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponsePostDto>> listOfDeletedAndType(@EnumValue(enumClass = Type.class) @PathVariable String type, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("type={}", type);
        Page<Post> posts = postService.listOfDeletedAndType(type, pageable);
        return getListResponseEntity(posts);
    }

    /**
     * 게시글 종류에 따라 전체 리스트 조회(커뮤니티/공지사항/이벤트)
     *
     * @param type     게시글 종류
     * @param pageable 페이징
     * @return 조회된 게시글 리스트
     */
    @Operation(summary = "게시글 종류에 따라 전체 리스트 조회(커뮤니티/공지사항/이벤트)")
    @Parameters(value = {
            @Parameter(name = "type", description = "종류(커뮤니티/공지사항/이벤트)", schema = @Schema(implementation = Type.class)),
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))), headers = {
            @Header(name = "TotalPages", description = "전체 페이지 개수", schema = @Schema(type = "string")),
            @Header(name = "TotalData", description = "전체 데이터 개수", schema = @Schema(type = "string"))
    })
    @GetMapping("/alltype/{type}")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponsePostDto>> listOfAllAndType(@EnumValue(enumClass = Type.class) @PathVariable String type, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("type={}", type);
        Page<Post> posts = postService.listOfAllAndType(type, pageable);
        return getListResponseEntity(posts);
    }

    /**
     * 삭제되지 않은 게시글 중 카테고리에 따라 게시글 리스트 조회
     *
     * @param category 게시글 카테고리
     * @param pageable 페이징
     * @return 조회된 게시글 리스트
     */
    @Operation(summary = "삭제되지 않은 게시글 중 카테고리에 따라 게시글 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "category", description = "카테고리 이름", example = "카테고리 이름"),
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))), headers = {
            @Header(name = "TotalPages", description = "전체 페이지 개수", schema = @Schema(type = "string")),
            @Header(name = "TotalData", description = "전체 데이터 개수", schema = @Schema(type = "string"))
    })
    @GetMapping("/notdeletedcategory/{category}")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponsePostDto>> listOfNotDeletedCategory(@PathVariable String category, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("category={}", category);
        Page<Post> posts = postService.listOfNotDeletedAndCategory(category, pageable);
        return getListResponseEntity(posts);
    }

    /**
     * 삭제된 게시글 중 카테고리에 따라 게시글 리스트 조회
     *
     * @param category 게시글 카테고리
     * @param pageable 페이징
     * @return 조회된 게시글 리스트
     */
    @Operation(summary = "삭제된 게시글 중 카테고리에 따라 게시글 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "category", description = "카테고리 이름", example = "카테고리 이름"),
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))), headers = {
            @Header(name = "TotalPages", description = "전체 페이지 개수", schema = @Schema(type = "string")),
            @Header(name = "TotalData", description = "전체 데이터 개수", schema = @Schema(type = "string"))
    })
    @GetMapping("/deletedcategory/{category}")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponsePostDto>> listOfDeletedCategory(@PathVariable String category, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("category={}", category);
        Page<Post> posts = postService.listOfDeletedAndCategory(category, pageable);
        return getListResponseEntity(posts);
    }

    /**
     * 카테고리에 따라 게시글 전체 리스트 조회
     *
     * @param category 게시글 카테고리
     * @param pageable 페이징
     * @return 조회된 게시글 리스트
     */
    @Operation(summary = "카테고리에 따라 게시글 전체 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "category", description = "카테고리 이름", example = "카테고리 이름"),
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))), headers = {
            @Header(name = "TotalPages", description = "전체 페이지 개수", schema = @Schema(type = "string")),
            @Header(name = "TotalData", description = "전체 데이터 개수", schema = @Schema(type = "string"))
    })
    @GetMapping("/allcategory/{category}")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponsePostDto>> listOfAllAndCategory(@PathVariable String category, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("category={}", category);
        Page<Post> posts = postService.listOfAllAndCategory(category, pageable);
        return getListResponseEntity(posts);
    }

    /**
     * 삭제되지 않은 게시글 조회
     *
     * @param orderBy   - 정렬 옵션 | postViews, postLikeNo, regDate
     * @param direction - asc, desc
     * @param pageable  - 페이징
     * @return - 조회된 게시글
     */
    @Operation(summary = "삭제되지 않은 게시글 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))), headers = {
            @Header(name = "TotalPages", description = "전체 페이지 개수", schema = @Schema(type = "string")),
            @Header(name = "TotalData", description = "전체 데이터 개수", schema = @Schema(type = "string"))
    })
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    @GetMapping
    public ResponseEntity<List<ResponsePostDto>> listOfNotDeleted(@RequestParam Optional<String> orderBy, @RequestParam Optional<String> direction, Pageable pageable) {

        if (orderBy.isPresent() && direction.isPresent()) {
            Sort.Direction dir = Sort.Direction.fromString(direction.get());
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(dir, orderBy.get()));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        }

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
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))), headers = {
            @Header(name = "TotalPages", description = "전체 페이지 개수", schema = @Schema(type = "string")),
            @Header(name = "TotalData", description = "전체 데이터 개수", schema = @Schema(type = "string"))
    })
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    @GetMapping("/deleted")
    public ResponseEntity<List<ResponsePostDto>> listOfDeleted(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
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
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))), headers = {
            @Header(name = "TotalPages", description = "전체 페이지 개수", schema = @Schema(type = "string")),
            @Header(name = "TotalData", description = "전체 데이터 개수", schema = @Schema(type = "string"))
    })
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    @GetMapping("/all")
    public ResponseEntity<List<ResponsePostDto>> allList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> posts = postService.allList(pageable);
        return getListResponseEntity(posts);
    }

    private ResponseEntity<ResponsePostDto> getResponseEntity(Long postId) {
        Post findPost = postService.findOnePost(postId);
        return ResponseEntity.ok(findPost.toResponsePostDto());
    }
}
