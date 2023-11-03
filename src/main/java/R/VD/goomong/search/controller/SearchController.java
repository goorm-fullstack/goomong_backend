package R.VD.goomong.search.controller;

import R.VD.goomong.search.dto.request.RequestItemSearchDTO;
import R.VD.goomong.search.dto.request.RequestPostSearchDTO;
import R.VD.goomong.search.dto.response.ResponseSearchDTO;
import R.VD.goomong.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/item")
    public ResponseEntity<ResponseSearchDTO> searchItem(RequestItemSearchDTO requestItemSearchDTO) {
        searchService.saveKeyword(requestItemSearchDTO);
        ResponseSearchDTO responseSearchDTO = searchService.searchItem(requestItemSearchDTO);
        return new ResponseEntity<>(responseSearchDTO, HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<ResponseSearchDTO> searchPost(RequestPostSearchDTO requestPostSearchDTO) {
        ResponseSearchDTO responseSearchDTO = searchService.searchPost(requestPostSearchDTO);
        return new ResponseEntity<>(responseSearchDTO, HttpStatus.OK);
    }
}