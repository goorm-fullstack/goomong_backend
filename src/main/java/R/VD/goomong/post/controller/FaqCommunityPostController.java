package R.VD.goomong.post.controller;

import R.VD.goomong.post.dto.request.RequestFaqCommunityPostDto;
import R.VD.goomong.post.dto.response.ResponseFaqCommunityPostDto;
import R.VD.goomong.post.service.FaqCommunityPostService;
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
@Tag(name = "FAQ/커뮤니티 생성 및 수정 api")
public class FaqCommunityPostController {

    private final FaqCommunityPostService faqCommunityPostService;

    /**
     * FAQ/커뮤니티 게시글 생성
     *
     * @param requestFaqCommunityPostDto 생성 request
     * @param images                     업로드한 이미지 리스트
     * @param files                      업로드한 파일 리스트
     * @param bindingResult              validation
     * @return 생성 성공 시 200
     */
    @Operation(summary = "FAQ/커뮤니티 게시글 생성")
    @Parameters(value = {
            @Parameter(name = "images", description = "업로드한 이미지 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile"))),
            @Parameter(name = "files", description = "업로드한 파일 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile"))),
            @Parameter(name = "bindingResult", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping(value = "/faqcommunitypost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> initFaqCommunityPost(@Validated @ModelAttribute RequestFaqCommunityPostDto requestFaqCommunityPostDto, MultipartFile[] images, MultipartFile[] files, BindingResult bindingResult) {
        log.info("requestFaqCommunityPostDto={}", requestFaqCommunityPostDto);

        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().build();

        faqCommunityPostService.saveFaqCommunityPost(requestFaqCommunityPostDto, images, files);
        return ResponseEntity.ok().build();
    }

    /**
     * FAQ/커뮤니티 게시글 수정
     *
     * @param postId                     수정할 게시글 pk
     * @param requestFaqCommunityPostDto 수정 내용
     * @param images                     수정할 이미지 리스트
     * @param files                      수정할 파일 리스트
     * @param bindingResult              validation
     * @return 수정된 게시글
     */
    @Operation(summary = "FAQ/커뮤니티 게시글 수정")
    @Parameters(value = {
            @Parameter(name = "postId", description = "수정할 게시글 id"),
            @Parameter(name = "images", description = "수정할 이미지 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile"))),
            @Parameter(name = "files", description = "수정할 파일 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile"))),
            @Parameter(name = "bindingResult", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseFaqCommunityPostDto.class)))
    @PutMapping(value = "/faqcommunitypost/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseFaqCommunityPostDto> updateFaqCommunityPost(@PathVariable Long postId, @Validated @ModelAttribute RequestFaqCommunityPostDto requestFaqCommunityPostDto, MultipartFile[] images, MultipartFile[] files, BindingResult bindingResult) {
        log.info("postId={}", postId);
        log.info("requestFaqCommunityPostDto={}", requestFaqCommunityPostDto);

        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().build();

        ResponseFaqCommunityPostDto responseFaqCommunityDto = faqCommunityPostService.updateFaqCommunityPost(postId, requestFaqCommunityPostDto, images, files).toResponseFaqCommunityDto();
        return ResponseEntity.ok(responseFaqCommunityDto);
    }
}
