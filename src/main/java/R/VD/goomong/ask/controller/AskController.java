package R.VD.goomong.ask.controller;

import R.VD.goomong.ask.dto.request.RequestAnswerDto;
import R.VD.goomong.ask.dto.request.RequestAskDto;
import R.VD.goomong.ask.dto.response.ResponseAnswerDto;
import R.VD.goomong.ask.dto.response.ResponseAskDto;
import R.VD.goomong.ask.model.Ask;
import R.VD.goomong.ask.service.AskService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/asks")
@Tag(name = "문의/답변 api")
public class AskController {

    private final AskService askService;

    private static ResponseEntity<List<ResponseAskDto>> getListResponseEntity(Page<Ask> asks) {
        long totalElements = asks.getTotalElements();
        int totalPages = asks.getTotalPages();

        return ResponseEntity.ok()
                .header("TotalPages", String.valueOf(totalPages))
                .header("TotalData", String.valueOf(totalElements))
                .body(asks.getContent().stream().map(Ask::toResponseAskDto).toList());
    }

    /**
     * 문의글 생성
     *
     * @param requestAskDto 문의글 생성 request
     * @param files         업로드 파일
     * @return 생성 완료 시 200
     */
    @Operation(summary = "문의글 생성")
    @Parameter(name = "files", description = "사용자가 등록한 파일")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping(value = "/ask", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> initAsk(@Validated @ModelAttribute RequestAskDto requestAskDto, @RequestParam(required = false) MultipartFile[] files) {
        log.info("requestAskDto={}", requestAskDto);

        askService.saveAsk(requestAskDto, files);
        return ResponseEntity.ok().build();
    }

    /**
     * 답글 생성
     *
     * @param requestAnswerDto 답글 생성 request
     * @param files            업로드 파일
     * @return 생성 완료 시 200
     */
    @Operation(summary = "답변글 생성")
    @Parameter(name = "files", description = "사용자가 등록한 파일")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping(value = "/answer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> initAnswer(@Validated @ModelAttribute RequestAnswerDto requestAnswerDto, @RequestParam(required = false) MultipartFile[] files) {
        log.info("requestAnswerDto={}", requestAnswerDto);

        askService.saveAnswer(requestAnswerDto, files);
        return ResponseEntity.ok().build();
    }

    /**
     * 문의글 수정
     *
     * @param askId         수정할 문의글 pk
     * @param requestAskDto 수정 내용
     * @param files         수정 업로드 파일
     * @return 수정된 문의글
     */
    @Operation(summary = "문의글 수정")
    @Parameters(value = {
            @Parameter(name = "askId", description = "수정할 문의글의 id"),
            @Parameter(name = "files", description = "사용자가 올린 파일")
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseAskDto.class)))
    @PutMapping(value = "/ask/{askId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseAskDto> updateAsk(@PathVariable Long askId, @Validated @ModelAttribute RequestAskDto requestAskDto, @RequestParam(required = false) MultipartFile[] files) {
        log.info("askId={}", askId);
        log.info("requestAskDto={}", requestAskDto);

        Ask ask = askService.updateAsk(askId, requestAskDto, files);
        return ResponseEntity.ok(ask.toResponseAskDto());
    }

    /**
     * 답글 수정
     *
     * @param answerId         수정할 답글 pk
     * @param requestAnswerDto 수정 내용
     * @param files            수정 업로드 파일
     * @return 수정된 답글
     */
    @Operation(summary = "답글 수정")
    @Parameters(value = {
            @Parameter(name = "answerId", description = "수정할 답변글 id"),
            @Parameter(name = "files", description = "사용자가 업로드한 파일")
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseAnswerDto.class)))
    @PutMapping(value = "/answer/{answerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseAnswerDto> updateAnswer(@PathVariable Long answerId, @Validated @ModelAttribute RequestAnswerDto requestAnswerDto, @RequestParam(required = false) MultipartFile[] files) {
        log.info("answerId={}", answerId);
        log.info("requestAnswerDto={}", requestAnswerDto);

        Ask ask = askService.updateAnswer(answerId, requestAnswerDto, files);
        return ResponseEntity.ok(ask.toResponseAnswerDto());
    }

    /**
     * 소프트딜리트
     *
     * @param askId 삭제할 글 pk
     * @return 삭제 완료 시 200
     */
    @Operation(summary = "문의글 소프트딜리트")
    @Parameter(name = "askId", description = "삭제할 문의글 id")
    @ApiResponse(responseCode = "200", description = "성공")
    @DeleteMapping("/ask/softdel/{askId}")
    public ResponseEntity<Object> softDelete(@PathVariable Long askId) {
        log.info("askId={}", askId);

        askService.softDelete(askId);
        return ResponseEntity.ok().build();
    }

    /**
     * 완전 삭제
     *
     * @param askId 삭제할 글 pk
     * @return 삭제 완료 시 200
     */
    @Hidden
    @DeleteMapping("/ask/del/{askId}")
    public ResponseEntity<Object> delete(@PathVariable Long askId) {
        log.info("askId={}", askId);

        askService.delete(askId);
        return ResponseEntity.ok().build();
    }

    /**
     * 문의글 조회
     *
     * @param askId 조회할 문의글 pk
     * @return 조회된 문의글
     */
    @Operation(summary = "특정 문의글 조회")
    @Parameter(name = "askId", description = "조회할 문의글 id")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseAskDto.class)))
    @GetMapping("/ask/{askId}")
    public ResponseEntity<ResponseAskDto> viewAsk(@PathVariable Long askId) {
        log.info("askId={}", askId);

        Ask oneAsk = askService.findOneAsk(askId);
        return ResponseEntity.ok(oneAsk.toResponseAskDto());
    }

    /**
     * 삭제되지 않은 문의글 조회
     *
     * @param pageable 페이징
     * @return 조회된 문의글
     */
    @Operation(summary = "삭제되지 않은 문의글 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10"),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0")
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseAskDto.class)))
    @GetMapping
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponseAskDto>> listOfNotDeleted(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Ask> asks = askService.listOfNotDeleted(pageable);

        return getListResponseEntity(asks);
    }

    /**
     * 삭제된 문의글 조회
     *
     * @param pageable 페이징
     * @return 조회된 문의글
     */
    @GetMapping("/del")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponseAskDto>> listOfDeleted(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Ask> asks = askService.listOfDeleted(pageable);

        return getListResponseEntity(asks);
    }

    /**
     * 문의글 전체 조회
     *
     * @param pageable 페이징
     * @return 조회된 문의글
     */
    @GetMapping("all")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponseAskDto>> allList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Ask> asks = askService.allList(pageable);

        return getListResponseEntity(asks);
    }
}
