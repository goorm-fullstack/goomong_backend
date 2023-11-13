package R.VD.goomong.post.controller;

import R.VD.goomong.post.dto.request.RequestPostCategoryDto;
import R.VD.goomong.post.dto.response.ResponsePostCategoryDto;
import R.VD.goomong.post.model.PostCategory;
import R.VD.goomong.post.service.PostCategoryService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/postcategorys")
@Slf4j
@Tag(name = "커뮤니티/FAQ 카테고리 api")
public class PostCategoryController {

    private final PostCategoryService postCategoryService;

    /**
     * 카테고리 생성
     *
     * @param requestPostCategoryDto 카테고리 생성 request
     * @param images                 카테고리 이미지
     * @return 생성 완료 시 200
     */
    @Operation(summary = "카테고리 생성")
    @Parameter(name = "images", description = "카테고리 이미지", array = @ArraySchema(schema = @Schema(type = "MultipartFile")))
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping(value = "/postcategory", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> initPostCategory(@Validated @ModelAttribute RequestPostCategoryDto requestPostCategoryDto, @RequestParam(required = false) MultipartFile[] images) {
        log.info("requestPostCategoryDto={}", requestPostCategoryDto);
        postCategoryService.savePostCategory(requestPostCategoryDto, images);
        return ResponseEntity.ok().build();
    }

    /**
     * 카테고리 수정
     *
     * @param postCategoryId         수정할 카테고리 pk
     * @param requestPostCategoryDto 수정할 내용
     * @param images                 수정할 이미지
     * @return 수정된 카테고리
     */
    @Operation(summary = "카테고리 수정")
    @Parameters(value = {
            @Parameter(name = "postCategoryId", description = "수정할 카테고리 id"),
            @Parameter(name = "images", description = "수정할 카테고리 이미지", array = @ArraySchema(schema = @Schema(type = "MultipartFile")))
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponsePostCategoryDto.class)))
    @PutMapping(value = "/postcategory/{postCategoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponsePostCategoryDto> modifyPostCategory(@PathVariable Long postCategoryId, @Validated @ModelAttribute RequestPostCategoryDto requestPostCategoryDto, @RequestParam(required = false) MultipartFile[] images) {
        log.info("postCategoryId={}", postCategoryId);
        log.info("requestPostCategoryDto={}", requestPostCategoryDto);
        PostCategory postCategory = postCategoryService.updatePostCategory(postCategoryId, requestPostCategoryDto, images);
        return ResponseEntity.ok(postCategory.toResponsePostCategoryDto());
    }

    /**
     * 카테고리 소프트딜리트
     *
     * @param postCategoryId - 삭제할 카테고리 pk
     * @return - 삭제 완료 시 200
     */
    @Operation(summary = "카테고리 삭제")
    @Parameter(name = "postCategoryId", description = "삭제할 카테고리 id")
    @ApiResponse(responseCode = "200", description = "성공")
    @DeleteMapping("/postcategory/softdel/{postCategoryId}")
    public ResponseEntity<Object> softDeletePostCategory(@PathVariable Long postCategoryId) {
        postCategoryService.softDeletePostCategory(postCategoryId);
        return ResponseEntity.ok().build();
    }

    /**
     * 카테고리 완전 삭제
     *
     * @param postCategoryId - 삭제할 카테고리 pk
     * @return - 삭제 완료 시 200
     */
    @Hidden
    @DeleteMapping("/postcategory/{postCategoryId}")
    public ResponseEntity<Object> deletePostCategory(@PathVariable Long postCategoryId) {
        postCategoryService.deletePostCategory(postCategoryId);
        return ResponseEntity.ok().build();
    }

    /**
     * 삭제된 카테고리 복구
     *
     * @param postCategoryId - 삭제할 카테고리 id
     * @return - 복구 완료 시 200
     */
    @Operation(summary = "삭제한 카테고리 복구")
    @Parameter(name = "postCategoryId", description = "복구할 카테고리 id")
    @ApiResponse(responseCode = "200", description = "성공")
    @PutMapping("/postcategory/undel/{postCategoryId}")
    public ResponseEntity<Object> unDelete(@PathVariable Long postCategoryId) {
        postCategoryService.unDelete(postCategoryId);
        return ResponseEntity.ok().build();
    }

    /**
     * 카테고리 조회
     *
     * @param postCategoryId - 조회할 카테고리 pk
     * @return - 조회된 카테고리
     */
    @Operation(summary = "특정 카테고리 조회")
    @Parameter(name = "postCategoryId", description = "조회할 카테고리 id")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponsePostCategoryDto.class)))
    @GetMapping("/{postCategoryId}")
    public ResponseEntity<ResponsePostCategoryDto> viewPostCategory(@PathVariable Long postCategoryId) {
        PostCategory onePostCategory = postCategoryService.findOnePostCategory(postCategoryId);
        return ResponseEntity.ok(onePostCategory.toResponsePostCategoryDto());
    }

    /**
     * 삭제되지 않은 카테고리 조회
     *
     * @return - 조회된 카테고리
     */
    @Operation(summary = "삭제되지 않은 카테고리 리스트 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostCategoryDto.class))))
    @GetMapping
    public ResponseEntity<List<ResponsePostCategoryDto>> listOfNotDeleted() {
        List<PostCategory> postCategories = postCategoryService.listOfNotDeleted();
        return ResponseEntity.ok(postCategories.stream().map(PostCategory::toResponsePostCategoryDto).toList());
    }

    /**
     * 삭제된 카테고리 조회
     *
     * @return - 조회된 카테고리
     */
    @Operation(summary = "삭제된 카테고리 리스트 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostCategoryDto.class))))
    @GetMapping("/deleted")
    public ResponseEntity<List<ResponsePostCategoryDto>> listOfDeleted() {
        List<PostCategory> postCategories = postCategoryService.listOfDeleted();
        return ResponseEntity.ok(postCategories.stream().map(PostCategory::toResponsePostCategoryDto).toList());
    }

    /**
     * 모든 카테고리 조회
     *
     * @return - 조회된 카테고리
     */
    @Operation(summary = "모든 카테고리 리스트 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsePostCategoryDto.class))))
    @GetMapping("/all")
    public ResponseEntity<List<ResponsePostCategoryDto>> allList() {
        List<PostCategory> postCategories = postCategoryService.allList();
        return ResponseEntity.ok(postCategories.stream().map(PostCategory::toResponsePostCategoryDto).toList());
    }
}
