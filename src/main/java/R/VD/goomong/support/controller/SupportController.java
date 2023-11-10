package R.VD.goomong.support.controller;

import R.VD.goomong.support.dto.request.RequestEmailAskDTO;
import R.VD.goomong.support.dto.response.ResponseEmailAskListDTO;
import R.VD.goomong.support.dto.response.ResponsePageWrap;
import R.VD.goomong.support.service.EmailAskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/email-asks")
    public ResponseEntity<Object> saveEmailAsk(RequestEmailAskDTO requestEmailAskDTo) {

        emailAskService.saveEmailAsk(requestEmailAskDTo);

        return ResponseEntity.ok().build();

    }
}