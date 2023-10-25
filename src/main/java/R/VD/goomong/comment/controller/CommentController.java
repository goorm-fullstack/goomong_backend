package R.VD.goomong.comment.controller;

import R.VD.goomong.comment.dto.request.RequestCommentDto;
import R.VD.goomong.comment.dto.response.ResponseCommentDto;
import R.VD.goomong.comment.model.Comment;
import R.VD.goomong.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    private static ResponseEntity<List<ResponseCommentDto>> getListResponseEntity(Page<Comment> comments) {
        long totalElements = comments.getTotalElements();
        int totalPages = comments.getTotalPages();
        return ResponseEntity.ok()
                .header("TotalPages", String.valueOf(totalPages))
                .header("TotalData", String.valueOf(totalElements))
                .body(comments.getContent().stream().map(Comment::toResponseCommentDto).toList());
    }

    /**
     * 댓글 작성
     *
     * @param requestCommentDto - 댓글 작성 request
     * @return - 작성 완료 시 200
     */
    @PostMapping("/comment")
    public ResponseEntity<Object> initComment(@Validated @RequestBody RequestCommentDto requestCommentDto) {
        commentService.saveComment(requestCommentDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 수정
     *
     * @param commentId         - 수정할 댓글 pk
     * @param requestCommentDto - 댓글 수정 내용
     * @return - 수정된 댓글
     */
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<ResponseCommentDto> updateComment(@PathVariable Long commentId, @Validated @RequestBody RequestCommentDto requestCommentDto) {
        Comment comment = commentService.updateComment(commentId, requestCommentDto);
        return ResponseEntity.ok(comment.toResponseCommentDto());
    }

    /**
     * 댓글 소프트딜리트
     *
     * @param commentId - 삭제할 댓글 pk
     * @return - 삭제 완료 시 200
     */
    @PutMapping("/comment/softdel/{commentId}")
    public ResponseEntity<Object> softDeleteComment(@PathVariable Long commentId) {
        commentService.softDeleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 완전삭제
     *
     * @param commentId - 삭제할 댓글 pk
     * @return - 삭제 완료 시 200
     */
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    /**
     * 삭제된 댓글 복구
     *
     * @param commentId - 복구할 댓글 pk
     * @return - 복구 완료 시 200
     */
    @PutMapping("/comment/undel/{commentId}")
    public ResponseEntity<Object> unDeleteComment(@PathVariable Long commentId) {
        commentService.unDelete(commentId);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 댓글 조회
     *
     * @param commentId - 조회할 댓글 pk
     * @return - 조회된 댓글
     */
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<ResponseCommentDto> viewComment(@PathVariable Long commentId) {
        Comment oneComment = commentService.findOneComment(commentId);
        return ResponseEntity.ok(oneComment.toResponseCommentDto());
    }

    /**
     * 댓글 좋아요 클릭
     *
     * @param commentId - 좋아요 클릭할 댓글 pk
     * @return - 댓글의 좋아요 수
     */
    @GetMapping("/comment/like/{commentId}")
    public ResponseEntity<Integer> increaseLike(@PathVariable Long commentId) {
        commentService.increaseCommentLike(commentId);
        Comment oneComment = commentService.findOneComment(commentId);
        return ResponseEntity.ok(oneComment.getLikeNo());
    }

    /**
     * 삭제되지 않은 댓글 조회
     *
     * @param pageable - 페이징
     * @return - 조회된 댓글
     */
    @GetMapping
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponseCommentDto>> listOfNotDeleted(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Comment> comments = commentService.listOfNotDeletedComment(pageable);
        return getListResponseEntity(comments);
    }

    /**
     * 삭제된 댓글 조회
     *
     * @param pageable - 페이징
     * @return - 조회된 댓글
     */
    @GetMapping("/deleted")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponseCommentDto>> listOfDeleted(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Comment> comments = commentService.listOfDeletedComment(pageable);
        return getListResponseEntity(comments);
    }

    /**
     * 전체 댓글 리스트(삭제된 것과 삭제 안된 것 모두 포함)
     *
     * @param pageable - 페이징
     * @return - 조회된 댓글
     */
    @GetMapping("/all")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponseCommentDto>> allList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Comment> comments = commentService.allList(pageable);
        return getListResponseEntity(comments);
    }
}
