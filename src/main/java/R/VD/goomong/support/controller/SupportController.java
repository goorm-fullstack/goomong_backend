package R.VD.goomong.support.controller;

import R.VD.goomong.chat.dto.response.ResponseChatRoomDTO;
import R.VD.goomong.global.model.ErrorResponseDTO;
import R.VD.goomong.support.dto.request.*;
import R.VD.goomong.support.dto.response.ResponseEmailAsk;
import R.VD.goomong.support.dto.response.ResponseEmailAskListDTO;
import R.VD.goomong.support.dto.response.ResponsePageWrap;
import R.VD.goomong.support.model.EmailAnswer;
import R.VD.goomong.support.service.EmailAnswerService;
import R.VD.goomong.support.service.EmailAskService;
import R.VD.goomong.support.service.EmailMemberSaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Support", description = "고객관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/support")
public class SupportController {

    private final EmailAskService emailAskService;
    private final EmailAnswerService emailAnswerService;
    private final EmailMemberSaveService emailMemberSaveService;

    @Operation(summary = "고객 문의 조회", description = "고객 이메일 문의를 조회합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "문의 조회 성공", content = @Content(schema = @Schema(implementation = ResponseEmailAskListDTO.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/email-asks")
    public ResponseEntity<ResponsePageWrap<List<ResponseEmailAskListDTO>>> getEmailAskList(Pageable pageable) {

        ResponsePageWrap<List<ResponseEmailAskListDTO>> emailAskList = emailAskService.getEmailAskList(pageable);

        return new ResponseEntity<>(emailAskList, HttpStatus.OK);

    }

    @Operation(summary = "고객 문의 상세 조회", description = "문의(id)를 이용해 내용을 조회합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "문의 조회 성공", content = @Content(schema = @Schema(implementation = ResponseEmailAsk.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/email-asks/{emailAskId}")
    public ResponseEntity<ResponseEmailAsk> getEmailAsk(@PathVariable Long emailAskId) {

        ResponseEmailAsk emailAsk = emailAskService.getEmailAsk(emailAskId);

        return new ResponseEntity<>(emailAsk, HttpStatus.OK);

    }

    @Operation(summary = "고객 문의 저장",
            description = "이메일(email, String), 제목(title, String), 내용(content, String), 파일(files)을 이용해 문의 메일 저장",
            responses = {
            @ApiResponse(responseCode = "200", description = "문의 조회 성공")
    })
    @PostMapping("/email-asks")
    public ResponseEntity<Object> saveEmailAsk(@ModelAttribute RequestEmailAskDTO requestEmailAskDTO) {

        log.info(requestEmailAskDTO.toString());

        emailAskService.saveEmailAsk(requestEmailAskDTO);

        return ResponseEntity.ok().build();

    }

    @Operation(summary = "고객 문의 답변",
            description = "문의 (Long, emailAskId), 관리자 (Long, adminId), 제목 (title, String), 내용(content, String), 파일(files)을 이용해 답변 메일",
            responses = {
            @ApiResponse(responseCode = "200", description = "답변 메일 작성 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/email-answer")
    public ResponseEntity<Object> createEmailAnswer(@RequestBody RequestEmailAnswerDTO emailAnswerDTO) {
        EmailAnswer emailAnswer = emailAnswerService.saveEmailAnswer(emailAnswerDTO);
        emailAnswerService.sendMail(emailAnswer);
        return ResponseEntity.ok().build();
    }

    //인증 메일 전송
    @Operation(summary = "이메일 인증 코드 메일",
            description = "이메일 (email, String)에 인증 메일 전송",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이메일 전송 성공")
            })
    @PostMapping("/sendCode")
    public ResponseEntity<Object> MailTest(@RequestBody RequestOnlyEmail requestOnlyEmail){
        emailMemberSaveService.sendMail(requestOnlyEmail.getEmail());

        return ResponseEntity.ok("send success");
    }

    //인증 코드 확인
    @Operation(summary = "이메일 인증 코드 확인",
            description = "이메일 (email, String), 인증 코드(code, String)로 코드 확인",
            responses = {
                    @ApiResponse(responseCode = "200", description = "인증 성공"),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
            })
    @PostMapping("/checkCode")
    public ResponseEntity<Object> checkCode(@RequestBody RequestCheckCode requestCheckCode) {
        emailMemberSaveService.checkCode(requestCheckCode);

        return ResponseEntity.ok("check success");
    }

    //이메일 변경
    @Operation(summary = "이메일 변경",
            description = "이전 이메일(oldEmail, String)을 현재 이메일(newEmail, String)으로 변경",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이메일 변경 성공"),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
            })
    @PutMapping("/emailUpdate")
    public ResponseEntity<Object> updateEmail(@RequestBody RequestUpdateEmail requestUpdateEmail) {
        emailMemberSaveService.updateEmail(requestUpdateEmail.getOldEmail(), requestUpdateEmail.getNewEmail());

        return ResponseEntity.ok("update success");
    }

}