package R.VD.goomong.review.controller;

import R.VD.goomong.global.model.PageInfo;
import R.VD.goomong.review.dto.request.RequestReviewDto;
import R.VD.goomong.review.dto.response.ResponseReviewDto;
import R.VD.goomong.review.model.Review;
import R.VD.goomong.review.service.ReviewService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/reviews")
@Tag(name = "리뷰 api")
public class ReviewController {
    private final ReviewService reviewService;

    private static ResponseEntity<List<ResponseReviewDto>> getListResponseEntity(Pageable pageable, Page<Review> reviews) {
        long totalElements = reviews.getTotalElements();
        int totalPages = reviews.getTotalPages();
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();

        List<ResponseReviewDto> list = new ArrayList<>();
        for (Review review : reviews.getContent()) {
            ResponseReviewDto responseReviewDto = review.toResponseReviewDto();
            PageInfo build = PageInfo.builder()
                    .page(pageNumber)
                    .size(pageSize)
                    .totalPage(totalPages)
                    .totalElements(totalElements)
                    .build();
            ResponseReviewDto build1 = responseReviewDto.toBuilder()
                    .pageInfo(build)
                    .build();
            list.add(build1);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 리뷰 작성
     *
     * @param requestReviewDto 리뷰 작성 request
     * @param images           리뷰 이미지
     * @return 작성 완료 시 200
     */
    @Operation(summary = "리뷰 작성")
    @Parameter(name = "images", description = "업로드할 이미지 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile")))
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping(value = "/review", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> initReview(@Validated @ModelAttribute RequestReviewDto requestReviewDto, @RequestParam(required = false) MultipartFile[] images) {
        log.info("requestReviewDto={}", requestReviewDto);

        reviewService.save(requestReviewDto, images);
        return ResponseEntity.ok().build();
    }

    /**
     * 리뷰 수정
     *
     * @param reviewId         수정할 리뷰 pk
     * @param requestReviewDto 수정 내용
     * @param images           수정할 이미지
     * @return 수정된 리뷰
     */
    @Operation(summary = "리뷰 수정")
    @Parameters(value = {
            @Parameter(name = "reviewId", description = "수정할 리뷰 id"),
            @Parameter(name = "images", description = "수정할 업로드 이미지 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile")))
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseReviewDto.class)))
    @PutMapping(value = "/review/{reviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseReviewDto> updateReview(@PathVariable Long reviewId, @Validated @ModelAttribute RequestReviewDto requestReviewDto, @RequestParam(required = false) MultipartFile[] images) {
        log.info("reviewId={}", reviewId);
        log.info("requestReviewDto={}", requestReviewDto);

        Review review = reviewService.updateReview(reviewId, requestReviewDto, images);
        return ResponseEntity.ok(review.toResponseReviewDto());
    }

    /**
     * 리뷰 소프트딜리트
     *
     * @param reviewId 삭제할 리뷰 pk
     * @return 삭제 완료 시 200
     */
    @Operation(summary = "리뷰 삭제")
    @Parameter(name = "reviewId", description = "삭제할 리뷰 id")
    @ApiResponse(responseCode = "200", description = "성공")
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<Object> deleteReview(@PathVariable Long reviewId) {
        log.info("reviewId={}", reviewId);

        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 리뷰 조회
     *
     * @param reviewId 조회할 리뷰 pk
     * @return 조회된 리뷰
     */
    @Operation(summary = "특정 리뷰 조회")
    @Parameter(name = "reviewId", description = "조회할 리뷰 id")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseReviewDto.class)))
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<ResponseReviewDto> viewReview(@PathVariable Long reviewId) {
        log.info("reviewId={}", reviewId);

        Review byId = reviewService.findById(reviewId);
        return ResponseEntity.ok(byId.toResponseReviewDto());
    }

    /**
     * 삭제되지 않은 리뷰 조회
     *
     * @param pageable 페이징
     * @return 조회된 리뷰
     */
    @Operation(summary = "삭제되지 않은 리뷰 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseReviewDto.class))))
    @GetMapping
    public ResponseEntity<List<ResponseReviewDto>> listOfNotDeleted(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Review> reviews = reviewService.listOfNotDeleted(pageable);
        return getListResponseEntity(pageable, reviews);
    }

    /**
     * 삭제된 리뷰 조회
     *
     * @param pageable 페이징
     * @return 조회된 리뷰
     */
    @Operation(summary = "삭제된 리뷰 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseReviewDto.class))))
    @GetMapping("/deleted")
    public ResponseEntity<List<ResponseReviewDto>> listOfDeleted(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Review> reviews = reviewService.listOfDeleted(pageable);
        return getListResponseEntity(pageable, reviews);
    }

    /**
     * 모든 리뷰 조회
     *
     * @param pageable 페이징
     * @return 조회된 리뷰
     */
    @Operation(summary = "모든 리뷰 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseReviewDto.class))))
    @GetMapping("/all")
    public ResponseEntity<List<ResponseReviewDto>> allList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Review> reviews = reviewService.allList(pageable);
        return getListResponseEntity(pageable, reviews);
    }

    /**
     * 전체 리뷰의 평균 평점 구하기
     *
     * @return 전체 리뷰의 평균 평점
     */
    @Operation(summary = "전체 리뷰 평균 평점 구하기")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Float.class)))
    @GetMapping("/aveRate")
    public ResponseEntity<String> aveRate() {
        return ResponseEntity.ok(reviewService.aveReview());
    }

    /**
     * 고객 만족도
     *
     * @return 고객 만족도
     */
    @Operation(summary = "고객 만족도 구하기")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Integer.class)))
    @GetMapping("/customerSatisfaction")
    public ResponseEntity<String> customerSatisfaction() {
        return ResponseEntity.ok(reviewService.customerSatisfaction());
    }

    /**
     * 베스트 리뷰 리스트
     *
     * @return 베스트 리뷰 리스트
     */
    @Operation(summary = "베스트 리뷰 리스트 구하기")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseReviewDto.class))))
    @GetMapping("/best")
    public ResponseEntity<List<ResponseReviewDto>> bestReview() {
        Page<ResponseReviewDto> responseReviewDtos = reviewService.bestReview(0, 9);
        return ResponseEntity.ok(responseReviewDtos.getContent());
    }
}
