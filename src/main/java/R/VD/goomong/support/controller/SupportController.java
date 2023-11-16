package R.VD.goomong.support.controller;

import R.VD.goomong.support.dto.request.*;
import R.VD.goomong.support.dto.response.ResponseEmailAsk;
import R.VD.goomong.support.dto.response.ResponseEmailAskListDTO;
import R.VD.goomong.support.dto.response.ResponsePageWrap;
import R.VD.goomong.support.model.EmailAnswer;
import R.VD.goomong.support.service.EmailAnswerService;
import R.VD.goomong.support.service.EmailAskService;
import R.VD.goomong.support.service.EmailMemberSaveService;
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
    private final EmailMemberSaveService emailMemberSaveService;

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

    //인증 메일 전송
    @PostMapping("/sendCode")
    public ResponseEntity<Object> MailTest(@RequestBody RequestOnlyEmail requestOnlyEmail){
        emailMemberSaveService.sendMail(requestOnlyEmail.getEmail());

        return ResponseEntity.ok("send success");
    }

    //인증 코드 확인
    @PostMapping("/checkCode")
    public ResponseEntity<Object> checkCode(@RequestBody RequestCheckCode requestCheckCode) {
        emailMemberSaveService.checkCode(requestCheckCode);

        return ResponseEntity.ok("check success");
    }

    //이메일 변경
    @PutMapping("/emailUpdate")
    public ResponseEntity<Object> updateEmail(@RequestBody RequestUpdateEmail requestUpdateEmail) {
        emailMemberSaveService.updateEmail(requestUpdateEmail.getOldEmail(), requestUpdateEmail.getNewEmail());

        return ResponseEntity.ok("update success");
    }
}