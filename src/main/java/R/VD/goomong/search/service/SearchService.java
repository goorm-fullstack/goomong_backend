package R.VD.goomong.search.service;

import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.search.Model.Search;
import R.VD.goomong.search.Model.Word;
import R.VD.goomong.search.dto.PageInfo;
import R.VD.goomong.search.dto.request.RequestSearchDTO;
import R.VD.goomong.search.dto.response.ResponseSearchDTO;
import R.VD.goomong.search.exception.SearchNotFoundException;
import R.VD.goomong.search.repository.ItemSearchRepository;
import R.VD.goomong.search.repository.SearchRepository;
import R.VD.goomong.search.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {

    private final ItemSearchRepository itemSearchRepository;
    private final SearchRepository searchRepository;
    private final WordRepository wordRepository;
    private final MemberRepository memberRepository;

    public void saveKeyword(RequestSearchDTO requestSearchDTO) {
        Long memberId = requestSearchDTO.getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new SearchNotFoundException("멤버 " + memberId + "는 존재하지 않습니다."));

        String keyword = requestSearchDTO.getKeyword();
        Word word = wordRepository.findByKeyword(keyword).orElseGet(() -> new Word(keyword));

        if (word.getWordId() == null)
            wordRepository.save(word);
        else
            wordRepository.incrementWordCount(keyword);

        Search search = Search.builder()
                .member(member)
                .word(word)
                .build();

        searchRepository.save(search);
    }

    public ResponseSearchDTO searchItem(RequestSearchDTO searchDTO) {
        int page = searchDTO.getPage() - 1;
        int pageSize = searchDTO.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Item> itemPage = itemSearchRepository.itemSearch(searchDTO.getKeyword(), searchDTO.getOrder(), searchDTO.getCategory(), pageable);

        PageInfo pageinfo = PageInfo.builder()
                .page(page)
                .size(pageSize)
                .totalElements((int) itemPage.getTotalElements())
                .totalPage(itemPage.getTotalPages())
                .build();

        List<Item> itemList = itemPage.getContent();
        List<ResponseItemDto> items = itemList.stream().map(ResponseItemDto::new).toList();
        return new ResponseSearchDTO(items, pageinfo);
    }

}