package R.VD.goomong.comment.controller;

import R.VD.goomong.comment.dto.request.RequestCommentDto;
import R.VD.goomong.comment.dto.response.ResponseCommentDto;
import R.VD.goomong.comment.model.Comment;
import R.VD.goomong.comment.service.CommentService;
import R.VD.goomong.global.model.PageInfo;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/comments")
@Tag(name = "댓글 api")
public class CommentController {

    private final CommentService commentService;

    private static List<ResponseCommentDto> getResponseCommentDtos(Pageable pageable, Page<Comment> comments) {
        long totalElements = comments.getTotalElements();
        int totalPages = comments.getTotalPages();
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();

        List<ResponseCommentDto> list = new ArrayList<>();
        for (Comment comment : comments.getContent()) {
            ResponseCommentDto responseCommentDto = comment.toResponseCommentDto();
            PageInfo build = PageInfo.builder()
                    .page(pageNumber)
                    .size(pageSize)
                    .totalPage(totalPages)
                    .totalElements(totalElements)
                    .build();
            ResponseCommentDto build1 = responseCommentDto.toBuilder()
                    .pageInfo(build)
                    .build();
            list.add(build1);
        }
        return list;
    }

    /**
     * 댓글 작성
     *
     * @param requestCommentDto 댓글 작성 request
     * @return 작성 완료 시 200
     */
    @Operation(summary = "댓글 작성")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping("/comment")
    public ResponseEntity<Object> initComment(@Validated @RequestBody RequestCommentDto requestCommentDto) {
        log.info("requestCommentDto={}", requestCommentDto);
        commentService.saveComment(requestCommentDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 수정
     *
     * @param commentId         수정할 댓글 pk
     * @param requestCommentDto 댓글 수정 내용
     * @return 수정된 댓글
     */
    @Operation(summary = "댓글 수정")
    @Parameter(name = "commentId", description = "수정할 댓글 id")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseCommentDto.class)))
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<ResponseCommentDto> updateComment(@PathVariable Long commentId, @Validated @RequestBody RequestCommentDto requestCommentDto) {
        log.info("commentId={}", commentId);
        log.info("requestCommentDto={}", requestCommentDto);
        Comment comment = commentService.updateComment(commentId, requestCommentDto);
        return ResponseEntity.ok(comment.toResponseCommentDto());
    }

    /**
     * 댓글 소프트딜리트
     *
     * @param commentId 삭제할 댓글 pk
     * @return 삭제 완료 시 200
     */
    @Operation(summary = "댓글 삭제")
    @Parameter(name = "commentId", description = "삭제할 댓글 id")
    @ApiResponse(responseCode = "200", description = "성공")
    @DeleteMapping("/comment/softdel/{commentId}")
    public ResponseEntity<Object> softDeleteComment(@PathVariable Long commentId) {
        log.info("commentId={}", commentId);
        commentService.softDeleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 완전삭제
     *
     * @param commentId 삭제할 댓글 pk
     * @return 삭제 완료 시 200
     */
    @Hidden
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable Long commentId) {
        log.info("commentId={}", commentId);
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    /**
     * 삭제된 댓글 복구
     *
     * @param commentId 복구할 댓글 pk
     * @return 복구 완료 시 200
     */
    @Operation(summary = "삭제된 댓글 복구")
    @Parameter(name = "commentId", description = "복구할 댓글 id")
    @ApiResponse(responseCode = "200", description = "성공")
    @PutMapping("/comment/undel/{commentId}")
    public ResponseEntity<Object> unDeleteComment(@PathVariable Long commentId) {
        log.info("commentId={}", commentId);
        commentService.unDelete(commentId);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 댓글 조회
     *
     * @param commentId 조회할 댓글 pk
     * @return 조회된 댓글
     */
    @Operation(summary = "특정 댓글 조회")
    @Parameter(name = "commentId", description = "조회할 댓글 id")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseCommentDto.class)))
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<ResponseCommentDto> viewComment(@PathVariable Long commentId) {
        log.info("commentId={}", commentId);
        Comment oneComment = commentService.findOneComment(commentId);
        return ResponseEntity.ok(oneComment.toResponseCommentDto());
    }

    /**
     * 삭제되지 않은 댓글 조회
     *
     * @param pageable 페이징
     * @return 조회된 댓글
     */
    @Operation(summary = "삭제되지 않은 댓글 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseCommentDto.class))))
    @GetMapping
    public ResponseEntity<List<ResponseCommentDto>> listOfNotDeleted(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Comment> comments = commentService.listOfNotDeletedComment(pageable);

        List<ResponseCommentDto> list = getResponseCommentDtos(pageable, comments);
        return ResponseEntity.ok(list);
    }

    /**
     * 회원 id로 댓글 리스트 조회
     *
     * @param pageable 페이징
     * @param memberId 회원 id
     * @return 조회된 댓글 리스트
     */
    @Operation(summary = "회원 id로 댓글 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true),
            @Parameter(name = "memberId", description = "회원 id", example = "1")
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseCommentDto.class))))
    @GetMapping("/{memberId}")
    public ResponseEntity<List<ResponseCommentDto>> listOfNotDeletedAndMemberId(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long memberId) {
        Page<Comment> comments = commentService.listOfNotDeletedAndMemberId(pageable, memberId);
        List<ResponseCommentDto> responseCommentDtos = getResponseCommentDtos(pageable, comments);
        return ResponseEntity.ok(responseCommentDtos);
    }

    /**
     * 삭제된 댓글 조회
     *
     * @param pageable 페이징
     * @return 조회된 댓글
     */
    @Operation(summary = "삭제된 댓글 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseCommentDto.class))))
    @GetMapping("/deleted")
    public ResponseEntity<List<ResponseCommentDto>> listOfDeleted(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Comment> comments = commentService.listOfDeletedComment(pageable);
        List<ResponseCommentDto> list = getResponseCommentDtos(pageable, comments);
        return ResponseEntity.ok(list);
    }

    /**
     * 전체 댓글 리스트(삭제된 것과 삭제 안된 것 모두 포함)
     *
     * @param pageable 페이징
     * @return 조회된 댓글
     */
    @Operation(summary = "전체 댓글 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseCommentDto.class))))
    @GetMapping("/all")
    public ResponseEntity<List<ResponseCommentDto>> allList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Comment> comments = commentService.allList(pageable);
        List<ResponseCommentDto> list = getResponseCommentDtos(pageable, comments);
        return ResponseEntity.ok(list);
    }

}
