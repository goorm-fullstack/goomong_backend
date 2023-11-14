package R.VD.goomong.support.service;

import R.VD.goomong.file.model.Files;
import R.VD.goomong.file.service.FilesService;
import R.VD.goomong.global.model.PageInfo;
import R.VD.goomong.support.dto.request.RequestEmailAskDTO;
import R.VD.goomong.support.dto.response.ResponseEmailAsk;
import R.VD.goomong.support.dto.response.ResponseEmailAskListDTO;
import R.VD.goomong.support.dto.response.ResponsePageWrap;
import R.VD.goomong.support.exception.SupportNotFoundException;
import R.VD.goomong.support.model.EmailAsk;
import R.VD.goomong.support.repository.EmailAskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailAskService {

    private final EmailAskRepository emailAskRepository;
    private final FilesService filesService;

    public ResponseEmailAsk getEmailAsk(Long emailAskId) {
        EmailAsk emailAsk = emailAskRepository.findById(emailAskId)
                .orElseThrow(() -> new SupportNotFoundException("고객 문의 " + emailAskId + " 는 존재하지 않습니다."));
        return new ResponseEmailAsk(emailAsk);
    }

    public ResponsePageWrap<List<ResponseEmailAskListDTO>> getEmailAskList(Pageable pageable) {

        Page<EmailAsk> all = emailAskRepository.findAll(pageable);

        PageInfo pageInfo = PageInfo.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalPage(all.getTotalPages())
                .totalElements(all.getTotalElements())
                .build();

        List<EmailAsk> content = all.getContent();
        List<ResponseEmailAskListDTO> emailAskListDTOList = content.stream()
                .map(ResponseEmailAskListDTO::new).toList();
        return new ResponsePageWrap<>(emailAskListDTOList, pageInfo);
    }

    public void saveEmailAsk(RequestEmailAskDTO requestEmailAskDTO) {

        List<Files> files = new ArrayList<>();
        
        MultipartFile[] uploadedFiles = requestEmailAskDTO.getFiles();
        if (uploadedFiles != null && uploadedFiles.length > 0) {
            files = filesService.saveFiles(requestEmailAskDTO.getFiles());
        }

        EmailAsk emailAsk = EmailAsk.builder()
                .email(requestEmailAskDTO.getEmail())
                .title(requestEmailAskDTO.getTitle())
                .content(requestEmailAskDTO.getContent())
                .filesList(files)
                .isEmailOpened(false)
                .build();

        emailAskRepository.save(emailAsk);

    }

}
