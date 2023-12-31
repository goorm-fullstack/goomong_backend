package R.VD.goomong.support.service;

import R.VD.goomong.file.model.Files;
import R.VD.goomong.file.service.FilesService;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.support.dto.request.RequestEmailAnswerDTO;
import R.VD.goomong.support.exception.SupportNotFoundException;
import R.VD.goomong.support.model.EmailAnswer;
import R.VD.goomong.support.model.EmailAsk;
import R.VD.goomong.support.repository.EmailAnswerRepository;
import R.VD.goomong.support.repository.EmailAskRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailAnswerService {

    private final EmailAskRepository emailAskRepository;
    private final EmailAnswerRepository emailAnswerRepository;
    private final MemberRepository memberRepository;
    private final FilesService filesService;

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Transactional
    public EmailAnswer saveEmailAnswer(RequestEmailAnswerDTO requestEmailAnswerDTO) {
        Long emailAskId = requestEmailAnswerDTO.getEmailAskId();
        Long adminId = requestEmailAnswerDTO.getAdminId();

        EmailAsk emailAsk = emailAskRepository.findById(emailAskId)
                .orElseThrow(() -> new SupportNotFoundException("문의 " + emailAskId + "는 존재하지 않습니다."));
        Member admin = memberRepository.findById(adminId)
                .orElseThrow(() -> new SupportNotFoundException("어드민 " + adminId + "는 존재하지 않습니다."));

        List<Files> files = new ArrayList<>();
        MultipartFile[] uploadedFiles = requestEmailAnswerDTO.getFiles();
        if (uploadedFiles != null && uploadedFiles.length > 0) {
            files = filesService.saveFiles(uploadedFiles);
        }
        
        EmailAnswer emailAnswer = EmailAnswer.builder()
                .emailAsk(emailAsk)
                .admin(admin)
                .title(requestEmailAnswerDTO.getTitle())
                .content(requestEmailAnswerDTO.getContent())
                .filesList(files)
                .build();

        emailAnswerRepository.save(emailAnswer);

        emailAsk.setEmailAnswer(emailAnswer);
        emailAskRepository.save(emailAsk);

        return emailAnswer;
    }

    public void sendMail(EmailAnswer emailAnswer) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            Context context = new Context();
            context.setVariable("emailAnswer", emailAnswer);

            String htmlContent = templateEngine.process("emailAnswer", context);

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(emailAnswer.getEmailAsk().getEmail());
            mimeMessageHelper.setSubject(emailAnswer.getTitle());
            mimeMessageHelper.setText(htmlContent, true);
            mimeMessageHelper.addInline("image-1", new ClassPathResource("/static/images/mail/image-1.png"));
            mimeMessageHelper.addInline("image-2", new ClassPathResource("/static/images/mail/image-2.png"));
            mimeMessageHelper.addInline("image-3", new ClassPathResource("/static/images/mail/image-3.png"));

            if (emailAnswer.getFilesList() != null && !emailAnswer.getFilesList().isEmpty()) {
                for (Files file : emailAnswer.getFilesList()) {
                    FileSystemResource fileResource = new FileSystemResource(file.getPath());
                    mimeMessageHelper.addAttachment(file.getFileName(), fileResource);
                }
            }

            javaMailSender.send(mimeMessage);

            log.info("email success");
        } catch (MessagingException e) {
            log.error("email fail {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }

}
