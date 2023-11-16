package R.VD.goomong.post.controller;

import R.VD.goomong.global.model.PageInfo;
import R.VD.goomong.post.dto.request.RequestAnswerForQuestionDto;
import R.VD.goomong.post.dto.request.RequestQuestionDto;
import R.VD.goomong.post.dto.response.ResponseAnswerForQuestionDto;
import R.VD.goomong.post.dto.response.ResponseQuestionDto;
import R.VD.goomong.post.model.Qna;
import R.VD.goomong.post.service.QnaService;
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
@RequestMapping("/api/qnas")
@Slf4j
@Tag(name = "QnA api")
public class QnaController {

    private final QnaService qnaService;

    private static List<ResponseQuestionDto> getResponseQuestionDtos(Pageable pageable, Page<Qna> qnas) {
        long totalElements = qnas.getTotalElements();
        int totalPages = qnas.getTotalPages();
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();

        List<ResponseQuestionDto> list = new ArrayList<>();
        for (Qna qna : qnas.getContent()) {
            ResponseQuestionDto responseQuestionDto = qna.toResponseQuestionDto();
            PageInfo build = PageInfo.builder()
                    .page(pageNumber)
                    .size(pageSize)
                    .totalPage(totalPages)
                    .totalElements(totalElements)
                    .build();
            ResponseQuestionDto build1 = responseQuestionDto.toBuilder()
                    .pageInfo(build)
                    .build();
            list.add(build1);
        }
        return list;
    }

    private static List<ResponseAnswerForQuestionDto> getResponseAnswerForQuestionDtos(Pageable pageable, Page<Qna> qnas) {
        long totalElements = qnas.getTotalElements();
        int totalPages = qnas.getTotalPages();
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();

        List<ResponseAnswerForQuestionDto> list = new ArrayList<>();
        for (Qna qna : qnas.getContent()) {
            ResponseAnswerForQuestionDto responseAnswerForQuestionDto = qna.toResponseAnswerForQuestionDto();
            PageInfo build = PageInfo.builder()
                    .page(pageNumber)
                    .size(pageSize)
                    .totalPage(totalPages)
                    .totalElements(totalElements)
                    .build();
            ResponseAnswerForQuestionDto build1 = responseAnswerForQuestionDto.toBuilder()
                    .pageInfo(build)
                    .build();
            list.add(build1);
        }
        return list;
    }

    /**
     * 질문 생성
     *
     * @param requestQuestionDto 질문 생성 request
     * @return 생성 완료 시 200
     */
    @Operation(summary = "질문 생성")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping("/qna/question")
    public ResponseEntity<Object> initQuestion(@Validated @RequestBody RequestQuestionDto requestQuestionDto) {
        log.info("requestQuestionDto={}", requestQuestionDto);
        qnaService.saveQuestion(requestQuestionDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 답변 생성
     *
     * @param requestAnswerForQuestionDto 답변 생성 reqeust
     * @return 생성 완료 시 200
     */
    @Operation(summary = "답변 생성")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping("/qna/answer")
    public ResponseEntity<Object> initAnswer(@Validated @RequestBody RequestAnswerForQuestionDto requestAnswerForQuestionDto) {
        log.info("requestAnswerForQuestionDto={}", requestAnswerForQuestionDto);
        qnaService.saveAnswer(requestAnswerForQuestionDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 질문 수정
     *
     * @param qnaId              수정할 질문 id
     * @param requestQuestionDto 수정 내용
     * @return 수정된 질문
     */
    @Operation(summary = "질문 수정")
    @Parameter(name = "qnaId", description = "수정할 질문 id", example = "1")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = RequestQuestionDto.class)))
    @PutMapping("/qna/question/{qnaId}")
    public ResponseEntity<ResponseQuestionDto> updateQuestion(@PathVariable Long qnaId, @Validated @RequestBody RequestQuestionDto requestQuestionDto) {
        log.info("qnaId={}", qnaId);
        log.info("requestQuestionDto={}", requestQuestionDto);

        Qna qna = qnaService.updateQuestion(qnaId, requestQuestionDto);
        return ResponseEntity.ok(qna.toResponseQuestionDto());
    }

    /**
     * 답변 수정
     *
     * @param qnaId                       수정할 답변 id
     * @param requestAnswerForQuestionDto 수정 내용
     * @return 수정된 답변
     */
    @Operation(summary = "답변 수정")
    @Parameter(name = "qnaId", description = "수정할 답변 id", example = "1")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseAnswerForQuestionDto.class)))
    @PutMapping("/qna/answer/{qnaId}")
    public ResponseEntity<ResponseAnswerForQuestionDto> updateAnswer(@PathVariable Long qnaId, @Validated @RequestBody RequestAnswerForQuestionDto requestAnswerForQuestionDto) {
        log.info("qnaId={}", qnaId);
        log.info("requestAnswerForQuestionDto={}", requestAnswerForQuestionDto);

        Qna qna = qnaService.updateAnswer(qnaId, requestAnswerForQuestionDto);
        return ResponseEntity.ok(qna.toResponseAnswerForQuestionDto());
    }

    /**
     * QnA 삭제
     *
     * @param qnaId 삭제할 QnA id
     * @return 삭제 완료 시 200
     */
    @Operation(summary = "QnA 삭제")
    @Parameter(name = "qnaId", description = "삭제할 QnA id", example = "1")
    @ApiResponse(responseCode = "200", description = "성공")
    @DeleteMapping("/qna/{qnaId}")
    public ResponseEntity<Object> deleteQna(@PathVariable Long qnaId) {
        log.info("qnaId={}", qnaId);
        qnaService.deleteQna(qnaId);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 질문 조회
     *
     * @param qnaId 조회할 질문 id
     * @return 조회된 질문
     */
    @Operation(summary = "특정 질문 조회")
    @Parameter(name = "qnaId", description = "조회할 질문 id", example = "1")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseQuestionDto.class)))
    @GetMapping("/qna/question/{qnaId}")
    public ResponseEntity<ResponseQuestionDto> findOneQuestion(@PathVariable Long qnaId) {
        log.info("qnaId={}", qnaId);
        Qna oneQna = qnaService.findOneQna(qnaId);
        return ResponseEntity.ok(oneQna.toResponseQuestionDto());
    }

    /**
     * 특정 답변 조회
     *
     * @param qnaId 조회할 답변 id
     * @return 조회된 답변
     */
    @Operation(summary = "특정 답변 조회")
    @Parameter(name = "qnaId", description = "조회할 답변 id", example = "1")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseAnswerForQuestionDto.class)))
    @GetMapping("/qna/answer/{qnaId}")
    public ResponseEntity<ResponseAnswerForQuestionDto> findOneAnswer(@PathVariable Long qnaId) {
        log.info("qnaId={}", qnaId);
        Qna oneQna = qnaService.findOneQna(qnaId);
        return ResponseEntity.ok(oneQna.toResponseAnswerForQuestionDto());
    }

    /**
     * 삭제되지 않은 질문 리스트 조회
     *
     * @param pageable 페이징
     * @return 조회된 질문 리스트
     */
    @Operation(summary = "삭제되지 않은 질문 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseQuestionDto.class))))
    @GetMapping("/questions")
    public ResponseEntity<List<ResponseQuestionDto>> listOfNotDeletedQuestions(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Qna> qnas = qnaService.listOfNotDeletedQuestion(pageable);
        List<ResponseQuestionDto> list = getResponseQuestionDtos(pageable, qnas);

        return ResponseEntity.ok(list);
    }

    /**
     * 삭제되지 않은 답변 리스트 조회
     *
     * @param pageable 페이징
     * @return 조회된 답변 리스트
     */
    @Operation(summary = "삭제되지 않은 답변 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseAnswerForQuestionDto.class))))
    @GetMapping("/answers")
    public ResponseEntity<List<ResponseAnswerForQuestionDto>> listOfNotDeletedAnswers(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Qna> qnas = qnaService.listOfNotDeletedAnswer(pageable);
        List<ResponseAnswerForQuestionDto> list = getResponseAnswerForQuestionDtos(pageable, qnas);

        return ResponseEntity.ok(list);
    }

    /**
     * 삭제된 질문 리스트 조회
     *
     * @param pageable 페이징
     * @return 조회된 질문 리스트
     */
    @Operation(summary = "삭제된 질문 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseQuestionDto.class))))
    @GetMapping("/deleted/questions")
    public ResponseEntity<List<ResponseQuestionDto>> listOfDeletedQuestions(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Qna> qnas = qnaService.listOfDeletedQuestion(pageable);
        List<ResponseQuestionDto> list = getResponseQuestionDtos(pageable, qnas);

        return ResponseEntity.ok(list);
    }

    /**
     * 삭제된 답변 리스트 조회
     *
     * @param pageable 페이징
     * @return 조회된 답변 리스트
     */
    @Operation(summary = "삭제된 답변 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseAnswerForQuestionDto.class))))
    @GetMapping("/deleted/answers")
    public ResponseEntity<List<ResponseAnswerForQuestionDto>> listOfDeletedAnswers(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Qna> qnas = qnaService.listOfDeletedAnswer(pageable);
        List<ResponseAnswerForQuestionDto> list = getResponseAnswerForQuestionDtos(pageable, qnas);

        return ResponseEntity.ok(list);
    }

    /**
     * 전체 QnA 리스트 조회
     *
     * @param pageable 페이징
     * @return 조회된 QnA 리스트
     */
    @Operation(summary = "전체 QnA 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseAnswerForQuestionDto.class))))
    @GetMapping("/all")
    public ResponseEntity<List<Object>> allList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Qna> qnas = qnaService.allList(pageable);

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        long totalElements = qnas.getTotalElements();
        int totalPages = qnas.getTotalPages();

        PageInfo pageInfo = PageInfo.builder()
                .size(pageSize)
                .page(pageNumber)
                .totalElements(totalElements)
                .totalPage(totalPages)
                .build();

        List<Object> list = new ArrayList<>();
        for (Qna qna : qnas.getContent()) {
            if (qna.getQna() == null) {
                ResponseQuestionDto responseQuestionDto = qna.toResponseQuestionDto();
                ResponseQuestionDto build = responseQuestionDto.toBuilder()
                        .pageInfo(pageInfo)
                        .build();
                list.add(build);
            } else {
                ResponseAnswerForQuestionDto responseAnswerForQuestionDto = qna.toResponseAnswerForQuestionDto();
                ResponseAnswerForQuestionDto build = responseAnswerForQuestionDto.toBuilder()
                        .pageInfo(pageInfo)
                        .build();
                list.add(build);
            }
        }
        return ResponseEntity.ok(list);
    }
}
