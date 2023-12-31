package R.VD.goomong.search.service;

import R.VD.goomong.global.model.PageInfo;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.post.dto.response.ResponsePostDto;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.search.dto.request.RequestSearchDTO;
import R.VD.goomong.search.dto.response.ResponseFindMemberDTO;
import R.VD.goomong.search.dto.response.ResponseRecentKeword;
import R.VD.goomong.search.dto.response.ResponseSearchDTO;
import R.VD.goomong.search.dto.response.ResponseTopSearchKeyword;
import R.VD.goomong.search.exception.SearchNotFoundException;
import R.VD.goomong.search.model.Search;
import R.VD.goomong.search.model.Word;
import R.VD.goomong.search.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {

    private final ItemSearchRepository itemSearchRepository;
    private final PostSearchRepository postSearchRepository;
    private final MemberSearchRepository memberSearchRepository;
    private final SearchRepository searchRepository;
    private final WordRepository wordRepository;
    private final MemberRepository memberRepository;

    public List<ResponseRecentKeword> getRecentSearchKeywords(Long memberId) {
        List<Search> recentSearch = searchRepository.findRecentSearchesByMemberId(memberId);

        return recentSearch.stream()
                .map(ResponseRecentKeword::new)
                .distinct()
                .toList();
    }

    public List<ResponseTopSearchKeyword> getTopSearchKeywords() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusHours(6);

        List<Object[]> topKeywords = searchRepository.findTopKeywordsNative(startTime, endTime);

        return topKeywords.stream()
                .map(result -> new ResponseTopSearchKeyword((String) result[0]))
                .toList();
    }

    public void saveKeyword(RequestSearchDTO requestSearchDTO) {
        Long memberId = requestSearchDTO.getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new SearchNotFoundException("멤버 " + memberId + "는 존재하지 않습니다."));

        String keyword = requestSearchDTO.getKeyword();
        Word word = new Word(keyword);

        wordRepository.save(word);

        Search search = Search.builder()
                .member(member)
                .word(word)
                .build();

        searchRepository.save(search);
    }

    public ResponseSearchDTO searchItem(Pageable pageable, RequestSearchDTO searchDTO) {
        Page<Item> itemPage = itemSearchRepository.itemSearch(searchDTO.getKeyword(), searchDTO.getOrder(), searchDTO.getCategoryTitle(), pageable);

        PageInfo pageinfo = PageInfo.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageNumber())
                .totalElements(itemPage.getTotalElements())
                .totalPage(itemPage.getTotalPages())
                .build();

        List<Item> itemList = itemPage.getContent();
        List<ResponseItemDto> items = itemList.stream().map(ResponseItemDto::new).toList();
        return new ResponseSearchDTO(items, pageinfo);
    }

    public ResponseSearchDTO searchPost(Pageable pageable, RequestSearchDTO searchDTO) {
        Page<Post> postPage = postSearchRepository.postSearch(searchDTO.getKeyword(), searchDTO.getOrder(), searchDTO.getCategoryTitle(), pageable);

        PageInfo pageInfo = PageInfo.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalElements(postPage.getTotalElements())
                .totalPage(postPage.getTotalPages())
                .build();

        List<Post> postList = postPage.getContent();
        List<ResponsePostDto> posts = postList.stream().map(Post::toResponsePostDto).toList();
        return new ResponseSearchDTO(posts, pageInfo);
    }

    public ResponseSearchDTO searchMember(Pageable pageable, RequestSearchDTO searchDTO) {
        Page<ResponseFindMemberDTO> memberPage = memberSearchRepository.memberSearch(searchDTO.getKeyword(), searchDTO.getOrder(), searchDTO.getCategoryTitle(), pageable);

        PageInfo pageInfo = PageInfo.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalElements(memberPage.getTotalElements())
                .totalPage(memberPage.getTotalPages())
                .build();

        List<ResponseFindMemberDTO> memberList = memberPage.getContent();
        return new ResponseSearchDTO(memberList, pageInfo);
    }

    public void deleteSearchBySearchId(Long searchId) {
        searchRepository.deleteById(searchId);
    }

    @Transactional
    public void deleteAllSearchesByMember(Long memberId) {
        searchRepository.deleteAllByMemberId(memberId);
    }
}