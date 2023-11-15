package R.VD.goomong.post.controller;

import R.VD.goomong.post.dto.request.RequestCategoryDto;
import R.VD.goomong.post.dto.response.ResponseCategoryDto;
import R.VD.goomong.post.model.Category;
import R.VD.goomong.post.service.CategoryService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categorys")
@Slf4j
@Tag(name = "커뮤니티/FAQ 카테고리 api")
public class CategoryController {

    private final CategoryService categoryService;

    private static ResponseEntity<List<ResponseCategoryDto>> getListResponseEntity(Page<Category> categories, Stream<Category> categories1) {
        long totalElements = categories.getTotalElements();
        int totalPages = categories.getTotalPages();
        return ResponseEntity.ok()
                .header("TotalPages", String.valueOf(totalPages))
                .header("TotalData", String.valueOf(totalElements))
                .body(categories1.map(Category::toResponseCategoryDto).toList());
    }

    /**
     * 카테고리 생성
     *
     * @param requestCategoryDto 카테고리 생성 request
     * @param images             카테고리 이미지
     * @return 생성 완료 시 200
     */
    @Operation(summary = "카테고리 생성")
    @Parameter(name = "images", description = "카테고리 이미지", array = @ArraySchema(schema = @Schema(type = "MultipartFile")))
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping(value = "/category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> initCategory(@Validated @ModelAttribute RequestCategoryDto requestCategoryDto, @RequestParam(required = false) MultipartFile[] images) {
        log.info("requestCategoryDto={}", requestCategoryDto);
        categoryService.saveCategory(requestCategoryDto, images);
        return ResponseEntity.ok().build();
    }

    /**
     * 카테고리 수정
     *
     * @param categoryId         수정할 카테고리 pk
     * @param requestCategoryDto 수정할 내용
     * @param images             수정할 이미지
     * @return 수정된 카테고리
     */
    @Operation(summary = "카테고리 수정")
    @Parameters(value = {
            @Parameter(name = "categoryId", description = "수정할 카테고리 id"),
            @Parameter(name = "images", description = "수정할 카테고리 이미지", array = @ArraySchema(schema = @Schema(type = "MultipartFile")))
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseCategoryDto.class)))
    @PutMapping(value = "/category/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseCategoryDto> modifyCategory(@PathVariable Long categoryId, @Validated @ModelAttribute RequestCategoryDto requestCategoryDto, @RequestParam(required = false) MultipartFile[] images) {
        log.info("categoryId={}", categoryId);
        log.info("requestCategoryDto={}", requestCategoryDto);
        Category category = categoryService.updateCategory(categoryId, requestCategoryDto, images);
        return ResponseEntity.ok(category.toResponseCategoryDto());
    }

    /**
     * 카테고리 소프트딜리트
     *
     * @param categoryId 삭제할 카테고리 pk
     * @return 삭제 완료 시 200
     */
    @Operation(summary = "카테고리 삭제")
    @Parameter(name = "categoryId", description = "삭제할 카테고리 id")
    @ApiResponse(responseCode = "200", description = "성공")
    @DeleteMapping("/category/softdel/{categoryId}")
    public ResponseEntity<Object> softDeleteCategory(@PathVariable Long categoryId) {
        log.info("categoryId={}", categoryId);
        categoryService.softDeleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }

    /**
     * 카테고리 완전 삭제
     *
     * @param categoryId 삭제할 카테고리 pk
     * @return 삭제 완료 시 200
     */
    @Hidden
    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long categoryId) {
        log.info("categoryId={}", categoryId);
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }

    /**
     * 삭제된 카테고리 복구
     *
     * @param categoryId 삭제할 카테고리 id
     * @return 복구 완료 시 200
     */
    @Operation(summary = "삭제한 카테고리 복구")
    @Parameter(name = "categoryId", description = "복구할 카테고리 id")
    @ApiResponse(responseCode = "200", description = "성공")
    @PutMapping("/category/undel/{categoryId}")
    public ResponseEntity<Object> unDelete(@PathVariable Long categoryId) {
        log.info("categoryId={}", categoryId);
        categoryService.unDelete(categoryId);
        return ResponseEntity.ok().build();
    }

    /**
     * 카테고리 조회
     *
     * @param categoryId 조회할 카테고리 pk
     * @return 조회된 카테고리
     */
    @Operation(summary = "특정 카테고리 조회")
    @Parameter(name = "categoryId", description = "조회할 카테고리 id")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseCategoryDto.class)))
    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseCategoryDto> viewCategory(@PathVariable Long categoryId) {
        log.info("categoryId={}", categoryId);
        Category oneCategory = categoryService.findOneCategory(categoryId);
        return ResponseEntity.ok(oneCategory.toResponseCategoryDto());
    }

    /**
     * 삭제되지 않은 카테고리 중 카테고리 이름으로 카테고리 리스트 조회
     *
     * @param categoryName 조회할 카테고리 이름
     * @param pageable     페이징
     * @return 조회된 카테고리 리스트
     */
    @Operation(summary = "삭제되지 않은 카테고리 중 카테고리 이름으로 카테고리 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "categoryName", description = "조회할 카테고리 이름", example = "카테고리 이름"),
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseCategoryDto.class))), headers = {
            @Header(name = "TotalPages", description = "전체 페이지 개수", schema = @Schema(type = "string")),
            @Header(name = "TotalData", description = "전체 데이터 개수", schema = @Schema(type = "string"))
    })
    @GetMapping("/notdeleted/name/{categoryName}")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponseCategoryDto>> listOfNotDeletedAndName(@PathVariable String categoryName, Pageable pageable) {
        Page<Category> categories = categoryService.listOfNotDeletedAndName(categoryName, pageable);

        return getListResponseEntity(categories, categories.getContent().stream());
    }

    /**
     * 삭제된 카테고리 중 카테고리 이름으로 카테고리 리스트 조회
     *
     * @param categoryName 조회할 카테고리 이름
     * @param pageable     페이징
     * @return 조회된 카테고리 리스트
     */
    @Operation(summary = "삭제된 카테고리 중 카테고리 이름으로 카테고리 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "categoryName", description = "조회할 카테고리 이름", example = "카테고리 이름"),
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseCategoryDto.class))), headers = {
            @Header(name = "TotalPages", description = "전체 페이지 개수", schema = @Schema(type = "string")),
            @Header(name = "TotalData", description = "전체 데이터 개수", schema = @Schema(type = "string"))
    })
    @GetMapping("/deleted/name/{categoryName}")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponseCategoryDto>> listOfDeletedAndName(@PathVariable String categoryName, Pageable pageable) {
        Page<Category> categories = categoryService.listOfDeletedAndName(categoryName, pageable);

        return getListResponseEntity(categories, categories.getContent().stream());
    }

    /**
     * 카테고리 이름으로 전체 카테고리 리스트 조회
     *
     * @param categoryName 조회할 카테고리 이름
     * @param pageable     페이징
     * @return 조회된 카테고리 리스트
     */
    @Operation(summary = "카테고리 이름으로 전체 카테고리 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "categoryName", description = "조회할 카테고리 이름", example = "카테고리 이름"),
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseCategoryDto.class))), headers = {
            @Header(name = "TotalPages", description = "전체 페이지 개수", schema = @Schema(type = "string")),
            @Header(name = "TotalData", description = "전체 데이터 개수", schema = @Schema(type = "string"))
    })
    @GetMapping("/name/{categoryName}")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponseCategoryDto>> listOfAllAndName(@PathVariable String categoryName, Pageable pageable) {
        Page<Category> categories = categoryService.listOfAllAndName(categoryName, pageable);

        return getListResponseEntity(categories, categories.getContent().stream());
    }

    /**
     * 삭제되지 않은 카테고리 조회
     *
     * @return 조회된 카테고리
     */
    @Operation(summary = "삭제되지 않은 카테고리 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseCategoryDto.class))), headers = {
            @Header(name = "TotalPages", description = "전체 페이지 개수", schema = @Schema(type = "string")),
            @Header(name = "TotalData", description = "전체 데이터 개수", schema = @Schema(type = "string"))
    })
    @GetMapping
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponseCategoryDto>> listOfNotDeleted(Pageable pageable) {
        Page<Category> categories = categoryService.listOfNotDeleted(pageable);

        return getListResponseEntity(categories, categories.getContent().stream());
    }

    /**
     * 삭제된 카테고리 조회
     *
     * @return 조회된 카테고리
     */
    @Operation(summary = "삭제된 카테고리 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseCategoryDto.class))), headers = {
            @Header(name = "TotalPages", description = "전체 페이지 개수", schema = @Schema(type = "string")),
            @Header(name = "TotalData", description = "전체 데이터 개수", schema = @Schema(type = "string"))
    })
    @GetMapping("/deleted")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponseCategoryDto>> listOfDeleted(Pageable pageable) {
        Page<Category> categories = categoryService.listOfDeleted(pageable);

        return getListResponseEntity(categories, categories.stream());
    }

    /**
     * 모든 카테고리 조회
     *
     * @return 조회된 카테고리
     */
    @Operation(summary = "모든 카테고리 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseCategoryDto.class))), headers = {
            @Header(name = "TotalPages", description = "전체 페이지 개수", schema = @Schema(type = "string")),
            @Header(name = "TotalData", description = "전체 데이터 개수", schema = @Schema(type = "string"))
    })
    @GetMapping("/all")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponseCategoryDto>> allList(Pageable pageable) {
        Page<Category> categories = categoryService.allList(pageable);

        return getListResponseEntity(categories, categories.stream());
    }
}
