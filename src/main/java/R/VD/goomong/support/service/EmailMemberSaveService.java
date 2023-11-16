package R.VD.goomong.support.service;

import R.VD.goomong.member.service.VerificationService;
import R.VD.goomong.support.dto.request.RequestCheckCode;
import R.VD.goomong.support.dto.request.RequestEmailMemberSaveDTO;
import R.VD.goomong.support.exception.SupportNotFoundException;
import R.VD.goomong.support.model.EmailMemberSave;
import R.VD.goomong.support.repository.EmailMemberSaveRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Service
@EnableAsync
@RequiredArgsConstructor
public class EmailMemberSaveService {

    private final JavaMailSender javaMailSender;
    private final VerificationService verificationService;
    private final EmailMemberSaveRepository emailMemberSaveRepository;
    private static final String senderEmail = "";       //이메일 입력

    //메일 작성
    public MimeMessage CreateMail(String email){
        MimeMessage message = javaMailSender.createMimeMessage();
        Optional<EmailMemberSave> byEmail = emailMemberSaveRepository.findByEmail(email);
        EmailMemberSave emailMemberSave = byEmail.get();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, emailMemberSave.getEmail());
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + emailMemberSave.getCode() + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }



        return message;
    }

    //인증 메일 전송
    public EmailMemberSave sendMail(String email){

        Optional<EmailMemberSave> byEmail = emailMemberSaveRepository.findByEmail(email);
        if(byEmail.isPresent()){
            EmailMemberSave emailMemberSave = byEmail.get();
            CreateMail(emailMemberSave.getEmail());
        }

        RequestEmailMemberSaveDTO requestEmailMemberSaveDTO = new RequestEmailMemberSaveDTO();
        requestEmailMemberSaveDTO.setEmail(email);
        requestEmailMemberSaveDTO.setCode(verificationService.createCode());
        requestEmailMemberSaveDTO.setEmailChecked(false);

        EmailMemberSave emailMemberSave = requestEmailMemberSaveDTO.toEntity();
        EmailMemberSave save = emailMemberSaveRepository.save(emailMemberSave);
        MimeMessage message = CreateMail(requestEmailMemberSaveDTO.getEmail());
        javaMailSender.send(message);

        return save;
    }

    //인증 코드 확인
    public EmailMemberSave checkCode(RequestCheckCode requestCheckCode) {
        Optional<EmailMemberSave> byEmail = emailMemberSaveRepository.findByEmail(requestCheckCode.getEmail());
        if(byEmail.isEmpty())                               //메일이 전송되지 않았을 때
            throw new SupportNotFoundException("인증 메일을 전송해주세요.");

        EmailMemberSave emailMemberSave = byEmail.get();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime regDate = emailMemberSave.getRegDate();
        long secondsDifference = ChronoUnit.SECONDS.between(regDate, now);
        if(secondsDifference > 300){                     //만료 시간 5분이 지났을 때
            throw new SupportNotFoundException("인증 시간이 지났습니다. 인증 메일을 재전송해주세요.");
        }

        if(!emailMemberSave.getCode().equals(requestCheckCode.getCode())){              //인증 코드가 같지 않을 때
            throw new SupportNotFoundException("인증 코드가 같지 않습니다. 코드를 다시 확인해주세요.");
        }

        emailMemberSave.emailCheckedSuccess();              //인증 코드가 같을 때
        return emailMemberSaveRepository.save(emailMemberSave);
    }

    //이메일 정보 변경
    public EmailMemberSave updateEmail(String email, String newEmail) {
        Optional<EmailMemberSave> byEmail = emailMemberSaveRepository.findByEmail(email);
        if(byEmail.isEmpty())
            throw new SupportNotFoundException("인증 되지 않은 이메일입니다.");

        EmailMemberSave emailMemberSave = byEmail.get();

        emailMemberSave.setEmail(newEmail);
        emailMemberSave.setEmailChecked(false);
        emailMemberSave.setCode("");

        return emailMemberSaveRepository.save(emailMemberSave);
    }
}
