package R.VD.goomong.post.controller;

import R.VD.goomong.global.model.PageInfo;
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

    private static List<ResponsePostDto> getResponsePostDtos(Pageable pageable, Page<Post> posts) {
        long totalElements = posts.getTotalElements();
        int totalPages = posts.getTotalPages();
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();

        List<ResponsePostDto> list = new ArrayList<>();
        for (Post post : posts.getContent()) {
            ResponsePostDto responsePostDto = post.toResponsePostDto();
            PageInfo build = PageInfo.builder()
                    .page(pageNumber)
                    .size(pageSize)
                    .totalPage(totalPages)
                    .totalElements(totalElements)
                    .build();
            ResponsePostDto build1 = responsePostDto.toBuilder()
                    .pageInfo(build)
                    .build();
            list.add(build1);
        }
        return list;
    }

    /**
     * 게시글 생성
     *
     * @param requestPostDto 생성 request
     * @param images         업로드한 이미지 리스트
     * @param files          업로드한 파일 리스트
     * @param isFix          고정 게시글 여부
     * @return 생성 성공 시 200
     */
    @Operation(summary = "게시글 생성")
    @Parameters(value = {
            @Parameter(name = "images", description = "업로드한 이미지 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile"))),
            @Parameter(name = "files", description = "업로드한 파일 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile"))),
            @Parameter(name = "isFix", description = "고정 게시글 여부", schema = @Schema(implementation = Boolean.class))
    })
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> initPost(@Validated @ModelAttribute RequestPostDto requestPostDto, MultipartFile[] images, MultipartFile[] files, @RequestParam(required = false) Boolean isFix) {
        log.info("requestPostDto={}", requestPostDto);

        postService.savePost(requestPostDto, images, files, isFix);
        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 수정
     *
     * @param postId         수정할 게시글 pk
     * @param requestPostDto 수정 내용
     * @param images         수정할 이미지 리스트
     * @param files          수정할 파일 리스트
     * @param isFix          고정 게시글 여부
     * @return 수정된 게시글
     */
    @Operation(summary = "게시글 수정")
    @Parameters(value = {
            @Parameter(name = "postId", description = "수정할 게시글 id"),
            @Parameter(name = "images", description = "수정할 이미지 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile"))),
            @Parameter(name = "files", description = "수정할 파일 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile"))),
            @Parameter(name = "isFix", description = "고정 게시글 여부", schema = @Schema(implementation = Boolean.class))
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponsePostDto.class)))
    @PutMapping(value = "/post/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponsePostDto> updatePost(@PathVariable Long postId, @Validated @ModelAttribute RequestPostDto requestPostDto, MultipartFile[] images, MultipartFile[] files, @RequestParam(required = false) Boolean isFix) {
        log.info("postId={}", postId);
        log.info("requestPostDto={}", requestPostDto);

        ResponsePostDto responsePostDto = postService.updatePost(postId, requestPostDto, images, files, isFix).toResponsePostDto();
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
     * 고정 게시글 조회
     *
     * @return 조회된 게시글
     */
    @Operation(summary = "고정된 게시글 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponsePostDto.class)))
    @GetMapping("/post/fixed")
    public ResponseEntity<?> fixedPost() {
        Post fixedPost = postService.findFixedPost();
        return ResponseEntity.ok(fixedPost != null ? fixedPost.toResponsePostDto() : null);
    }

    /**
     * 삭제되지 않은 게시글 중 게시글 종류에 따라 리스트 조회(커뮤니티/공지사항/이벤트)
     *
     * @param type     게시글 종류
     * @param region   조회할 지역
     * @param pageable 페이징
     * @return 조회된 게시글 리스트
     */
    @Operation(summary = "삭제되지 않은 게시글 중 게시글 종류에 따라 리스트 조회(커뮤니티/공지사항/이벤트)")
    @Parameters(value = {
            @Parameter(name = "type", description = "종류(커뮤니티/공지사항/이벤트)", schema = @Schema(implementation = Type.class)),
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true),
            @Parameter(name = "region", description = "조회하고자 하는 지역", example = "지역")
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))))
    @GetMapping("/notdeletedtype/{type}")
    public ResponseEntity<List<ResponsePostDto>> listOfNotDeletedAndType(@RequestParam Optional<String> orderBy, @RequestParam Optional<String> direction,
                                                                         @EnumValue(enumClass = Type.class) @PathVariable String type, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(required = false) String region) {
        log.info("type={}, region={}", type, region);

        pageable = getPageable(orderBy, direction, pageable);

        Page<Post> posts = postService.listOfNotDeletedAndType(type, region, pageable);
        List<ResponsePostDto> list = getResponsePostDtos(pageable, posts);

        return ResponseEntity.ok(list);
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
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))))
    @GetMapping("/deletedtype/{type}")
    public ResponseEntity<List<ResponsePostDto>> listOfDeletedAndType(@EnumValue(enumClass = Type.class) @PathVariable String type, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("type={}", type);
        Page<Post> posts = postService.listOfDeletedAndType(type, pageable);
        List<ResponsePostDto> list = getResponsePostDtos(pageable, posts);

        return ResponseEntity.ok(list);
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
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))))
    @GetMapping("/alltype/{type}")
    public ResponseEntity<List<ResponsePostDto>> listOfAllAndType(@RequestParam Optional<String> orderBy, @RequestParam Optional<String> direction,
                                                                  @EnumValue(enumClass = Type.class) @PathVariable String type, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("type={}", type);

        pageable = getPageable(orderBy, direction, pageable);

        Page<Post> posts = postService.listOfAllAndType(type, pageable);
        List<ResponsePostDto> list = getResponsePostDtos(pageable, posts);

        return ResponseEntity.ok(list);
    }

    /**
     * 삭제되지 않은 게시글 중 카테고리에 따라 게시글 리스트 조회
     *
     * @param category 게시글 카테고리
     * @param pageable 페이징
     * @param region   조회할 지역
     * @return 조회된 게시글 리스트
     */
    @Operation(summary = "삭제되지 않은 게시글 중 카테고리에 따라 게시글 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "category", description = "카테고리 이름", example = "카테고리 이름"),
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true),
            @Parameter(name = "region", description = "조회할 지역", example = "지역")
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))))
    @GetMapping("/notdeletedcategory/{category}")
    public ResponseEntity<List<ResponsePostDto>> listOfNotDeletedCategory(@RequestParam Optional<String> orderBy, @RequestParam Optional<String> direction,
                                                                          @PathVariable String category, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(required = false) String region) {
        log.info("category={}, region={}", category, region);

        pageable = getPageable(orderBy, direction, pageable);

        Page<Post> posts = postService.listOfNotDeletedAndCategory(category, region, pageable);
        List<ResponsePostDto> list = getResponsePostDtos(pageable, posts);

        return ResponseEntity.ok(list);
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
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))))
    @GetMapping("/deletedcategory/{category}")
    public ResponseEntity<List<ResponsePostDto>> listOfDeletedCategory(@PathVariable String category, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("category={}", category);
        Page<Post> posts = postService.listOfDeletedAndCategory(category, pageable);
        List<ResponsePostDto> list = getResponsePostDtos(pageable, posts);

        return ResponseEntity.ok(list);
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
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))))
    @GetMapping("/allcategory/{category}")
    public ResponseEntity<List<ResponsePostDto>> listOfAllAndCategory(@RequestParam Optional<String> orderBy, @RequestParam Optional<String> direction,
                                                                      @PathVariable String category, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("category={}", category);

        pageable = getPageable(orderBy, direction, pageable);

        Page<Post> posts = postService.listOfAllAndCategory(category, pageable);
        List<ResponsePostDto> list = getResponsePostDtos(pageable, posts);

        return ResponseEntity.ok(list);
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
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))))
    @GetMapping
    public ResponseEntity<List<ResponsePostDto>> listOfNotDeleted(@RequestParam Optional<String> orderBy, @RequestParam Optional<String> direction,
                                                                  @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        pageable = getPageable(orderBy, direction, pageable);

        Page<Post> posts = postService.listOfNotDeleted(pageable);
        List<ResponsePostDto> list = getResponsePostDtos(pageable, posts);

        return ResponseEntity.ok(list);
    }

    /**
     * 회원 id로 게시글 리스트 조회
     *
     * @param pageable 페이징
     * @param memberId 회원 id
     * @return 조회된 게시글 리스트
     */
    @Operation(summary = "회원 id로 게시글 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true),
            @Parameter(name = "memberId", description = "회원 id", example = "1")
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))))
    @GetMapping("/{memberId}")
    public ResponseEntity<List<ResponsePostDto>> listOfNotDeletedAndMemberId(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long memberId) {
        Page<Post> posts = postService.listOfNotDeletedAndMemberId(pageable, memberId);
        List<ResponsePostDto> responsePostDtos = getResponsePostDtos(pageable, posts);
        return ResponseEntity.ok(responsePostDtos);
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
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))))
    @GetMapping("/deleted")
    public ResponseEntity<List<ResponsePostDto>> listOfDeleted(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> posts = postService.listOfDeleted(pageable);
        List<ResponsePostDto> list = getResponsePostDtos(pageable, posts);

        return ResponseEntity.ok(list);
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
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))))
    @GetMapping("/all")
    public ResponseEntity<List<ResponsePostDto>> allList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> posts = postService.allList(pageable);
        List<ResponsePostDto> list = getResponsePostDtos(pageable, posts);

        return ResponseEntity.ok(list);
    }

    /**
     * hot 커뮤니티 게시글 기능
     *
     * @return hot 커뮤니티 게시글 리스트
     */
    @Operation(summary = "hot 커뮤니티 게시글 리스트")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))))
    @GetMapping("/hot")
    public ResponseEntity<List<ResponsePostDto>> hotPost() {
        Page<ResponsePostDto> responsePostDtos = postService.hotPost(0, 6);
        return ResponseEntity.ok(responsePostDtos.getContent());
    }

    /**
     * 특정 id의 데이터를 제외하고 랜덤 공지사항 게시글 리스트 가져오기
     *
     * @param postId 제외할 데이터의 id
     * @return 조회된 공지사항 게시글 리스트
     */
    @Operation(summary = "특정 id의 데이터를 제외하고 랜덤 공지사항 게시글 리스트 가져오기")
    @Parameter(name = "postId", description = "제외할 데이터의 id", example = "1")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostDto.class))))
    @GetMapping("/random/{postId}")
    public ResponseEntity<List<ResponsePostDto>> random(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.random(postId).stream().map(Post::toResponsePostDto).toList());
    }

    private ResponseEntity<ResponsePostDto> getResponseEntity(Long postId) {
        Post findPost = postService.findOnePost(postId);
        return ResponseEntity.ok(findPost.toResponsePostDto());
    }

    // 정렬에 따른 pageable settings
    private Pageable getPageable(Optional<String> orderBy, Optional<String> direction, Pageable pageable) {
        if (orderBy.isPresent() && direction.isPresent()) {
            Sort.Direction dir = Sort.Direction.fromString(direction.get());
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(dir, orderBy.get()));
        }
        return pageable;
    }
}
