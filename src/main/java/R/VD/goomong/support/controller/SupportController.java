package R.VD.goomong.support.controller;

import R.VD.goomong.support.dto.request.RequestEmailAnswerDTO;
import R.VD.goomong.support.dto.request.RequestEmailAskDTO;
import R.VD.goomong.support.dto.response.ResponseEmailAsk;
import R.VD.goomong.support.dto.response.ResponseEmailAskListDTO;
import R.VD.goomong.support.dto.response.ResponsePageWrap;
import R.VD.goomong.support.model.EmailAnswer;
import R.VD.goomong.support.service.EmailAnswerService;
import R.VD.goomong.support.service.EmailAskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/support")
public class SupportController {

    private final EmailAskService emailAskService;
    private final EmailAnswerService emailAnswerService;

    @GetMapping("/email-asks")
    public ResponseEntity<ResponsePageWrap<List<ResponseEmailAskListDTO>>> getEmailAskList(Pageable pageable) {

        ResponsePageWrap<List<ResponseEmailAskListDTO>> emailAskList = emailAskService.getEmailAskList(pageable);

        return new ResponseEntity<>(emailAskList, HttpStatus.OK);

    }

    @GetMapping("/email-asks/{emailAskId}")
    public ResponseEntity<ResponseEmailAsk> getEmailAsk(@PathVariable Long emailAskId) {

        ResponseEmailAsk emailAsk = emailAskService.getEmailAsk(emailAskId);

        return new ResponseEntity<>(emailAsk, HttpStatus.OK);

    }

    @PostMapping("/email-asks")
    public ResponseEntity<Object> saveEmailAsk(@ModelAttribute RequestEmailAskDTO requestEmailAskDTO) {

        log.info(requestEmailAskDTO.toString());

        emailAskService.saveEmailAsk(requestEmailAskDTO);

        return ResponseEntity.ok().build();

    }

    @PostMapping("/email-answer")
    public ResponseEntity<Object> createEmailAnswer(@RequestBody RequestEmailAnswerDTO emailAnswerDTO) {
        EmailAnswer emailAnswer = emailAnswerService.saveEmailAnswer(emailAnswerDTO);
        emailAnswerService.sendMail(emailAnswer);
        return ResponseEntity.ok().build();
    }
}