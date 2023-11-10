package R.VD.goomong.support.controller;

import R.VD.goomong.support.dto.request.RequestEmailAskDTO;
import R.VD.goomong.support.dto.response.ResponseEmailAsk;
import R.VD.goomong.support.dto.response.ResponseEmailAskListDTO;
import R.VD.goomong.support.dto.response.ResponsePageWrap;
import R.VD.goomong.support.service.EmailAskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/support")
public class SupportController {

    private final EmailAskService emailAskService;

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
    public ResponseEntity<Object> saveEmailAsk(@RequestBody RequestEmailAskDTO requestEmailAskDTo) {

        emailAskService.saveEmailAsk(requestEmailAskDTo);

        return ResponseEntity.ok().build();

    }
}