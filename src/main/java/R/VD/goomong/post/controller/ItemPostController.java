package R.VD.goomong.post.controller;

import R.VD.goomong.post.dto.request.RequestItemPostDto;
import R.VD.goomong.post.dto.response.ResponseItemPostDto;
import R.VD.goomong.post.service.ItemPostService;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/posts")
@Tag(name = "판매/기부/교환 게시글 생성 및 수정 api")
public class ItemPostController {

    private final ItemPostService itemPostService;

    /**
     * 게시글 생성
     *
     * @param requestPostDto 게시글 생성 request
     * @param images         게시글 이미지(상품 이미지와는 별개로)
     * @param files          게시글 첨부파일
     * @param bindingResult  validation
     * @return 성공적으로 작성 시 200
     */
    @Operation(summary = "판매/기부/교환 게시글 생성")
    @Parameters(value = {
            @Parameter(name = "images", description = "상품 정보에 있는 이미지 이외의 이미지 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile"))),
            @Parameter(name = "files", description = "업로드한 파일 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile")))
    })
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping(value = "/itempost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> initPost(@Validated @ModelAttribute RequestItemPostDto requestPostDto, @RequestParam(required = false) MultipartFile[] images, @RequestParam(required = false) MultipartFile[] files, BindingResult bindingResult) {

        log.info("requestPostDto={}", requestPostDto.toString());

        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().build();

        itemPostService.saveItemPost(requestPostDto, images, files);
        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 수정
     *
     * @param postId         수정할 게시글의 pk
     * @param requestPostDto 수정할 내용
     * @param images         수정할 이미지
     * @param files          수정할 파일
     * @param bindingResult  validation
     * @return 수정된 게시글
     */
    @Operation(summary = "판매/기부/교환 게시글 수정")
    @Parameters(value = {
            @Parameter(name = "postId", description = "수정할 게시글 id"),
            @Parameter(name = "images", description = "수정할 이미지 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile"))),
            @Parameter(name = "files", description = "수정할 파일 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile")))
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseItemPostDto.class)))
    @PutMapping(value = "/itempost/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseItemPostDto> modifyPost(@PathVariable Long postId, @Validated @ModelAttribute RequestItemPostDto requestPostDto, @RequestParam(required = false) MultipartFile[] images, @RequestParam(required = false) MultipartFile[] files, BindingResult bindingResult) {

        log.info("postId={}", postId);
        log.info("requestPostDto={}", requestPostDto.toString());

        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().build();

        ResponseItemPostDto responsePostDto = itemPostService.updateItemPost(postId, requestPostDto, images, files).toResponseItemPostDto();
        return ResponseEntity.ok(responsePostDto);
    }
}
