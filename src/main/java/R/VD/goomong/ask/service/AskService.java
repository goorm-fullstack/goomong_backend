package goomong.ask.service;

import goomong.ask.dto.request.RequestAnswerDto;
import goomong.ask.dto.request.RequestAskDto;
import goomong.ask.exception.NotFoundAsk;
import goomong.ask.model.Ask;
import goomong.ask.repository.AskRepository;
import goomong.item.exception.NotFoundItem;
import goomong.item.model.Item;
import goomong.item.repository.ItemRepository;
import goomong.member.exception.NotFoundMember;
import goomong.member.model.Member;
import goomong.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AskService {
    private final AskRepository askRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 문의 작성
    public void saveAsk(RequestAskDto requestAskDto) {
        Ask ask = requestAskDto.toEntity();
        Optional<Member> askMember = memberRepository.findById(requestAskDto.getMemberId());
        if (askMember.isEmpty()) {
            throw new NotFoundMember("존재하지 않는 사용자입니다.");
        }

        Optional<Item> askItem = itemRepository.findById(requestAskDto.getItemId());
        if (askItem.isEmpty()) {
            throw new NotFoundItem("존재하지 않는 상품입니다.");
        }
        Member findMember = askMember.get();
        Item findItem = askItem.get();
        ask.setMember(findMember);
        ask.setItem(findItem);
        Ask save = askRepository.save(ask);
        findItem.getAskList().add(save);
        findMember.getAskList().add(save);
    }


    // 문의에 대한 답변 작성
    public void saveAnswer(RequestAnswerDto requestAnswerDto) {
        Ask ask = requestAnswerDto.toEntity();
        Optional<Item> askItem = itemRepository.findById(requestAnswerDto.getItemId());
        if (askItem.isEmpty()) {
            throw new NotFoundItem("존재하지 않는 상품입니다.");
        }

        Optional<Ask> parentAsk = askRepository.findById(requestAnswerDto.getParentId());
        if (parentAsk.isEmpty()) {
            throw new NotFoundAsk("존재하지 않는 문의입니다.");
        }

        Item findItem = askItem.get();
        Ask findAsk = parentAsk.get();
        ask.setItem(findItem);
        ask.setParentAsk(findAsk);
        Ask save = askRepository.save(ask);
        findItem.getAskList().add(save);
        findAsk.getAsks().add(save);
    }
}
