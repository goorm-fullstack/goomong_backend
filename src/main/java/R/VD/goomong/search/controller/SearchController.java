package R.VD.goomong.search.controller;

import R.VD.goomong.search.dto.request.RequestSearchDTO;
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

    @PostMapping
    public ResponseEntity<ResponseSearchDTO> searchItem(RequestSearchDTO requestSearchDTO) {
        searchService.saveKeyword(requestSearchDTO);
        ResponseSearchDTO responseSearchDTO = searchService.searchItem(requestSearchDTO);
        return new ResponseEntity<>(responseSearchDTO, HttpStatus.OK);
    }

}