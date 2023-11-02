package R.VD.goomong.search.controller;

import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.search.dto.request.RequestSearchDTO;
import R.VD.goomong.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @PostMapping
    public ResponseEntity<List<ResponseItemDto>> searchItem(RequestSearchDTO requestSearchDTO) {
        searchService.saveKeyword(requestSearchDTO);
        List<ResponseItemDto> responseItems = searchService.searchItem(requestSearchDTO);
        return new ResponseEntity<>(responseItems, HttpStatus.OK);
    }

}