package R.VD.goomong.ask.service;

import R.VD.goomong.ask.dto.request.RequestAnswerDto;
import R.VD.goomong.ask.dto.request.RequestAskDto;
import R.VD.goomong.ask.exception.AlreadyDeletedAskException;
import R.VD.goomong.ask.exception.NotFoundAsk;
import R.VD.goomong.ask.model.Ask;
import R.VD.goomong.ask.repository.AskRepository;
import R.VD.goomong.file.model.Files;
import R.VD.goomong.file.service.FilesService;
import R.VD.goomong.item.exception.NotFoundItem;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.repository.ItemRepository;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AskService {

    private final AskRepository askRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final FilesService filesService;

    // 문의 작성
    public void saveAsk(RequestAskDto requestAskDto, MultipartFile[] files) {
        Ask ask = requestAskDto.toEntity();

        Member member = memberRepository.findById(requestAskDto.getMemberId()).orElseThrow(() -> new RuntimeException("해당 id의 회원을 찾을 수 없습니다. id = " + requestAskDto.getMemberId()));
        if (member.getDelDate() != null) throw new RuntimeException("해당 id의 회원은 삭제된 회원입니다. id = " + member.getId());

        Item item = itemRepository.findById(requestAskDto.getItemId()).orElseThrow(() -> new NotFoundItem("해당 id의 상품을 찾을 수 없습니다. id = " + requestAskDto.getItemId()));
        if (item.getDelDate() != null) throw new RuntimeException("해당 id의 상품은 삭제된 상품입니다. id = " + item.getId());

        List<Files> filesList = new ArrayList<>();
        if (files.length != 0) filesList = filesService.saveFiles(files);

        Ask build = ask.toBuilder()
                .member(member)
                .item(item)
                .filesList(filesList)
                .build();
        askRepository.save(build);
    }


    // 문의에 대한 답변 작성
    public void saveAnswer(RequestAnswerDto requestAnswerDto, MultipartFile[] files) {
        Ask ask = requestAnswerDto.toEntity();

        Member member = memberRepository.findById(requestAnswerDto.getMemberId()).orElseThrow(() -> new RuntimeException("해당 id의 회원을 찾을 수 없습니다. id = " + requestAnswerDto.getMemberId()));
        if (member.getDelDate() != null) throw new RuntimeException("해당 id의 회원은 삭제된 회원입니다. id = " + member.getId());

        Ask parent = askRepository.findById(requestAnswerDto.getParentId()).orElseThrow(() -> new NotFoundAsk("해당 id의 문의글을 찾을 수 없습니다. id = " + requestAnswerDto.getParentId()));
        if (parent.getDelDate() != null)
            throw new AlreadyDeletedAskException("해당 id의 문의글은 삭제된 문의글입니다. id = " + parent.getId());

        List<Files> filesList = new ArrayList<>();
        if (files.length != 0) filesList = filesService.saveFiles(files);

        Ask build = ask.toBuilder()
                .member(member)
                .ask(parent)
                .filesList(filesList)
                .build();
        askRepository.save(build);
    }

    // 문의글 수정
    public Ask updateAsk(Long askId, RequestAskDto requestAskDto, MultipartFile[] files) {
        Ask origin = askRepository.findById(askId).orElseThrow(() -> new NotFoundAsk("해당 id의 문의글을 찾을 수 없습니다. id = " + askId));
        if (origin.getDelDate() != null) throw new AlreadyDeletedAskException("해당 id의 문의글은 삭제된 문의글입니다. id = " + askId);

        List<Files> filesList = origin.getFilesList();
        if (files.length != 0) filesList = filesService.saveFiles(files);

        Ask build = origin.toBuilder()
                .title(requestAskDto.getTitle())
                .content(requestAskDto.getContent())
                .filesList(filesList)
                .build();
        return askRepository.save(build);
    }

    // 답변글 수정
    public Ask updateAnswer(Long answerId, RequestAnswerDto requestAnswerDto, MultipartFile[] files) {
        Ask origin = askRepository.findById(answerId).orElseThrow(() -> new NotFoundAsk("해당 id의 답변글을 찾을 수 없습니다. id = " + answerId));
        if (origin.getDelDate() != null)
            throw new AlreadyDeletedAskException("해당 id의 답변글은 삭제된 답변글입니다. id = " + answerId);

        List<Files> filesList = origin.getFilesList();
        if (files.length != 0) filesList = filesService.saveFiles(files);

        Ask build = origin.toBuilder()
                .title(requestAnswerDto.getTitle())
                .content(requestAnswerDto.getContent())
                .filesList(filesList)
                .build();
        return askRepository.save(build);
    }

    // 소프트딜리트
    public void softDelete(Long askId) {
        Ask ask = askRepository.findById(askId).orElseThrow(() -> new NotFoundAsk("해당 id의 글을 찾을 수 없습니다. id = " + askId));
        if (ask.getDelDate() != null) throw new AlreadyDeletedAskException("해당 id의 글은 이미 삭제된 글입니다. id = " + askId);

        Ask build = ask.toBuilder()
                .delDate(LocalDateTime.now())
                .build();
        askRepository.save(build);
    }
}
