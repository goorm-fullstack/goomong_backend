package R.VD.goomong.ask.controller;

import R.VD.goomong.ask.dto.request.RequestAnswerDto;
import R.VD.goomong.ask.dto.request.RequestAskDto;
import R.VD.goomong.ask.dto.response.ResponseAnswerDto;
import R.VD.goomong.ask.dto.response.ResponseAskDto;
import R.VD.goomong.ask.model.Ask;
import R.VD.goomong.ask.service.AskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/asks")
public class AskController {

    private final AskService askService;

    /**
     * 문의글 생성
     *
     * @param requestAskDto - 문의글 생성 request
     * @param files         - 업로드 파일
     * @return - 생성 완료 시 200
     */
    @PostMapping("/ask")
    public ResponseEntity<Object> initAsk(@Validated @ModelAttribute RequestAskDto requestAskDto, @RequestParam(required = false) MultipartFile[] files) {
        log.info("requestAskDto={}", requestAskDto);

        askService.saveAsk(requestAskDto, files);
        return ResponseEntity.ok().build();
    }

    /**
     * 답글 생성
     *
     * @param requestAnswerDto - 답글 생성 request
     * @param files            - 업로드 파일
     * @return - 생성 완료 시 200
     */
    @PostMapping("/answer")
    public ResponseEntity<Object> initAnswer(@Validated @ModelAttribute RequestAnswerDto requestAnswerDto, @RequestParam(required = false) MultipartFile[] files) {
        log.info("requestAnswerDto={}", requestAnswerDto);

        askService.saveAnswer(requestAnswerDto, files);
        return ResponseEntity.ok().build();
    }

    /**
     * 문의글 수정
     *
     * @param askId         - 수정할 문의글 pk
     * @param requestAskDto - 수정 내용
     * @param files         - 수정 업로드 파일
     * @return - 수정된 문의글
     */
    @PostMapping("/ask/{askId}")
    public ResponseEntity<ResponseAskDto> updateAsk(@PathVariable Long askId, @Validated @ModelAttribute RequestAskDto requestAskDto, @RequestParam(required = false) MultipartFile[] files) {
        log.info("askId={}", askId);
        log.info("requestAskDto={}", requestAskDto);

        Ask ask = askService.updateAsk(askId, requestAskDto, files);
        return ResponseEntity.ok(ask.toResponseAskDto());
    }

    /**
     * 답글 수정
     *
     * @param answerId         - 수정할 답글 pk
     * @param requestAnswerDto - 수정 내용
     * @param files            - 수정 업로드 파일
     * @return - 수정된 답글
     */
    @PostMapping("/answer/{answerId}")
    public ResponseEntity<ResponseAnswerDto> updateAnswer(@PathVariable Long answerId, @Validated @ModelAttribute RequestAnswerDto requestAnswerDto, @RequestParam(required = false) MultipartFile[] files) {
        log.info("answerId={}", answerId);
        log.info("requestAnswerDto={}", requestAnswerDto);

        Ask ask = askService.updateAnswer(answerId, requestAnswerDto, files);
        return ResponseEntity.ok(ask.toResponseAnswerDto());
    }
}
