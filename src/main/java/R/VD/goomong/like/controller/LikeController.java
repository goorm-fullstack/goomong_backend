package R.VD.goomong.like.controller;

import R.VD.goomong.like.dto.request.RequestCommentLikeDto;
import R.VD.goomong.like.dto.request.RequestPostLikeDto;
import R.VD.goomong.like.dto.request.RequestReviewLikeDto;
import R.VD.goomong.like.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
@Slf4j
@Tag(name = "좋아요 기능 api")
public class LikeController {

    private final LikeService likeService;

    /**
     * 게시글 좋아요 클릭
     *
     * @param requestPostLikeDto 게시글 좋아요 클릭 시 request
     * @return 성공 시 200
     */
    @Operation(summary = "게시글 좋아요 클릭")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping("/post")
    public ResponseEntity<Object> likePost(@RequestBody RequestPostLikeDto requestPostLikeDto) {
        log.info("requestPostLikeDto={}", requestPostLikeDto);
        likeService.likePostInsert(requestPostLikeDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 좋아요 클릭
     *
     * @param requestCommentLikeDto 댓글 좋아요 클릭 시 request
     * @return 성공 시 200
     */
    @Operation(summary = "댓글 좋아요 클릭")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping("/comment")
    public ResponseEntity<Object> likeComment(@RequestBody RequestCommentLikeDto requestCommentLikeDto) {
        log.info("requestCommentLikeDto={}", requestCommentLikeDto);
        likeService.likeCommentInsert(requestCommentLikeDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 리뷰 좋아요 클릭
     *
     * @param requestReviewLikeDto 리뷰 좋아요 클릭 시 request
     * @return 성공 시 200
     */
    @Operation(summary = "리뷰 좋아요 클릭")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping("/review")
    public ResponseEntity<Object> likeReview(@RequestBody RequestReviewLikeDto requestReviewLikeDto) {
        log.info("requestReviewLikeDto={}", requestReviewLikeDto);
        likeService.likeReviewInsert(requestReviewLikeDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 좋아요 취소
     *
     * @param requestPostLikeDto 취소 시 request
     * @return 성공 시 200
     */
    @Operation(summary = "게시글 좋아요 취소")
    @ApiResponse(responseCode = "200", description = "성공")
    @DeleteMapping("/post")
    public ResponseEntity<Object> dislikePost(@RequestBody RequestPostLikeDto requestPostLikeDto) {
        log.info("requestPostLikeDto={}", requestPostLikeDto);
        likeService.likePostCancel(requestPostLikeDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 좋아요 취소
     *
     * @param requestCommentLikeDto 취소 시 request
     * @return 성공 시 200
     */
    @Operation(summary = "댓글 좋아요 취소")
    @ApiResponse(responseCode = "200", description = "성공")
    @DeleteMapping("/comment")
    public ResponseEntity<Object> dislikeComment(@RequestBody RequestCommentLikeDto requestCommentLikeDto) {
        log.info("requestCommentLikeDto={}", requestCommentLikeDto);
        likeService.likeCommentCancel(requestCommentLikeDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 리뷰 좋아요 취소
     *
     * @param requestReviewLikeDto 취소 시 request
     * @return 성공 시 200
     */
    @Operation(summary = "리뷰 좋아요 취소")
    @ApiResponse(responseCode = "200", description = "성공")
    @DeleteMapping("/review")
    public ResponseEntity<Object> dislikeReview(@RequestBody RequestReviewLikeDto requestReviewLikeDto) {
        log.info("requestReviewLikeDto={}", requestReviewLikeDto);
        likeService.likeReviewCancel(requestReviewLikeDto);
        return ResponseEntity.ok().build();
    }
}
