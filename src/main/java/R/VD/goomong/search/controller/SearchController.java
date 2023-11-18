package R.VD.goomong.search.controller;

import R.VD.goomong.global.model.ErrorResponseDTO;
import R.VD.goomong.search.dto.request.RequestItemSearchDTO;
import R.VD.goomong.search.dto.request.RequestSearchDTO;
import R.VD.goomong.search.dto.response.ResponseSearchDTO;
import R.VD.goomong.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Search", description = "검색 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "상품 검색", description = "검색을 위해 memberId와 keyword, order, category를 받습니다.", responses = {
            @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ResponseSearchDTO.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/item")
    public ResponseEntity<ResponseSearchDTO> searchItem(Pageable pageable, RequestItemSearchDTO requestItemSearchDTO) {
        searchService.saveKeyword(requestItemSearchDTO);
        ResponseSearchDTO responseSearchDTO = searchService.searchItem(pageable, requestItemSearchDTO);
        return new ResponseEntity<>(responseSearchDTO, HttpStatus.OK);
    }

    @Operation(summary = "게시물 검색", description = "검색을 위해 keyword, order, category를 받습니다.", responses = {
            @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ResponseSearchDTO.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/post")
    public ResponseEntity<ResponseSearchDTO> searchPost(Pageable pageable, RequestSearchDTO requestSearchDTO) {
        ResponseSearchDTO responseSearchDTO = searchService.searchPost(pageable, requestSearchDTO);
        return new ResponseEntity<>(responseSearchDTO, HttpStatus.OK);
    }

    @Operation(summary = "멤버 검색", description = "검색을 위해 memberId와 keyword, order, category를 받습니다.", responses = {
            @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ResponseSearchDTO.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/member")
    public ResponseEntity<ResponseSearchDTO> searchMember(Pageable pageable, RequestSearchDTO requestSearchDTO) {
        ResponseSearchDTO responseSearchDTO = searchService.searchMember(pageable, requestSearchDTO);
        return new ResponseEntity<>(responseSearchDTO, HttpStatus.OK);
    }

}