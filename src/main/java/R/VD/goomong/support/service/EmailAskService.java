package R.VD.goomong.support.service;

import R.VD.goomong.global.model.PageInfo;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailAskService {

    private final EmailAskRepository emailAskRepository;
    private final MemberRepository memberRepository;

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

        EmailAsk emailAsk = EmailAsk.builder()
                .email(requestEmailAskDTO.getEmail())
                .title(requestEmailAskDTO.getTitle())
                .content(requestEmailAskDTO.getContent())
                .isEmailOpened(false)
                .build();

        if (requestEmailAskDTO.getMemberId() != null) {
            Long memberId = requestEmailAskDTO.getMemberId();
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new SupportNotFoundException("멤버 " + memberId + " 는 존재하지 않습니다."));
            emailAsk.setMember(member);
        }

        emailAskRepository.save(emailAsk);
    }


}
