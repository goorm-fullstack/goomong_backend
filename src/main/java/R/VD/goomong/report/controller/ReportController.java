package R.VD.goomong.report.controller;

import R.VD.goomong.global.model.PageInfo;
import R.VD.goomong.report.dto.request.RequestReportDto;
import R.VD.goomong.report.dto.response.ResponseReportDto;
import R.VD.goomong.report.model.Report;
import R.VD.goomong.report.service.ReportService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/reports")
@Tag(name = "신고글 api")
public class ReportController {

    private final ReportService reportService;

    private static ResponseEntity<List<ResponseReportDto>> getListResponseReportDto(Pageable pageable, Page<Report> reports) {
        long totalElements = reports.getTotalElements();
        int totalPages = reports.getTotalPages();
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();

        List<ResponseReportDto> list = new ArrayList<>();
        for (Report report : reports.getContent()) {
            ResponseReportDto responseReportDto = report.toResponseReportDto();
            PageInfo build = PageInfo.builder()
                    .page(pageNumber)
                    .size(pageSize)
                    .totalPage(totalPages)
                    .totalElements(totalElements)
                    .build();
            ResponseReportDto build1 = responseReportDto.toBuilder()
                    .pageInfo(build)
                    .build();
            list.add(build1);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 신고글 생성
     *
     * @param requestReportDto 신고글 생성 request
     * @param files            신고글 파일 업로드
     * @return 생성 완료 시 200
     */
    @Operation(summary = "신고글 생성")
    @Parameter(name = "files", description = "업로드할 파일 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile")))
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping(value = "/report", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> initReport(@Validated @ModelAttribute RequestReportDto requestReportDto, @RequestParam(required = false) MultipartFile[] files) {
        log.info("requestReportDto={}", requestReportDto);
        reportService.saveReport(requestReportDto, files);
        return ResponseEntity.ok().build();
    }

    /**
     * 신고글 수정
     *
     * @param reportId         수정할 신고글 pk
     * @param requestReportDto 수정할 내용
     * @param files            수정할 업로드 파일
     * @return 수정된 신고글
     */
    @Operation(summary = "신고글 수정")
    @Parameters(value = {
            @Parameter(name = "reportId", description = "수정할 신고글 id"),
            @Parameter(name = "files", description = "수정할 업로드 파일 리스트", array = @ArraySchema(schema = @Schema(type = "MultipartFile")))
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseReportDto.class)))
    @PutMapping(value = "/report/{reportId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseReportDto> updateReport(@PathVariable Long reportId, @Validated @ModelAttribute RequestReportDto requestReportDto, @RequestParam(required = false) MultipartFile[] files) {
        log.info("reportId={}", reportId);
        log.info("requestReportDto={}", requestReportDto);
        Report report = reportService.updateReport(reportId, requestReportDto, files);
        return ResponseEntity.ok(report.toResponseReportDto());
    }

    /**
     * 신고글 소프트딜리트
     *
     * @param reportId 삭제할 신고글 pk
     * @return 삭제 완료 시 200
     */
    @Operation(summary = "신고글 삭제")
    @Parameter(name = "reportId", description = "삭제할 신고글 id")
    @ApiResponse(responseCode = "200", description = "성공")
    @DeleteMapping("/report/softdel/{reportId}")
    public ResponseEntity<Object> softDelete(@PathVariable Long reportId) {
        log.info("reportId={}", reportId);
        reportService.softDeleteReport(reportId);
        return ResponseEntity.ok().build();
    }

    /**
     * 신고글 완전 삭제
     *
     * @param reportId 삭제할 신고글 pk
     * @return 삭제 완료 시 200
     */
    @Hidden
    @DeleteMapping("/report/{reportId}")
    public ResponseEntity<Object> delete(@PathVariable Long reportId) {
        log.info("reportId={}", reportId);
        reportService.deleteReport(reportId);
        return ResponseEntity.ok().build();
    }

    /**
     * 삭제된 신고글 복구
     *
     * @param reportId 복구할 신고글 pk
     * @return 복구 완료 시 200
     */
    @Operation(summary = "삭제된 신고글 복구")
    @Parameter(name = "reportId", description = "복구할 신고글 id")
    @ApiResponse(responseCode = "200", description = "성공")
    @PutMapping("/report/undel/{reportId}")
    public ResponseEntity<Object> unDelete(@PathVariable Long reportId) {
        log.info("reportId={}", reportId);
        reportService.unDeleteReport(reportId);
        return ResponseEntity.ok().build();
    }

    /**
     * 신고글 이상 없음 처리
     *
     * @param reportId 처리할 신고글 pk
     * @return 처리 완료 시 200
     */
    @Operation(summary = "신고 받은 글에 대한 이상 없음 처리")
    @Parameter(name = "reportId", description = "처리할 신고글 id")
    @ApiResponse(responseCode = "200", description = "성공")
    @PutMapping("/report/noproblem/{reportId}")
    public ResponseEntity<Object> checkNoProblem(@PathVariable Long reportId) {
        log.info("reportId={}", reportId);
        reportService.noProblemReport(reportId);
        return ResponseEntity.ok().build();
    }

    /**
     * 신고글 삭제 처리
     *
     * @param reportId 삭제 처리할 신고글 pk
     * @return 처리 완료 시 200
     */
    @Operation(summary = "신고 받은 글에 대한 삭제처리")
    @Parameter(name = "reportId", description = "처리할 신고글 id")
    @ApiResponse(responseCode = "200", description = "성공")
    @PutMapping("/report/del/{reportId}")
    public ResponseEntity<Object> checkDel(@PathVariable Long reportId) {
        log.info("reportId={}", reportId);
        reportService.checkReportDelete(reportId);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 신고글 조회
     *
     * @param reportId 조회할 신고글 pk
     * @return 조회된 신고글
     */
    @Operation(summary = "특정 신고글 조회")
    @Parameter(name = "reportId", description = "조회할 신고글 id")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseReportDto.class)))
    @GetMapping("/report/{reportId}")
    public ResponseEntity<ResponseReportDto> viewReport(@PathVariable Long reportId) {
        log.info("reportId={}", reportId);
        Report oneReport = reportService.findOneReport(reportId);
        return ResponseEntity.ok(oneReport.toResponseReportDto());
    }

    /**
     * 삭제되지 않은 신고글 조회
     *
     * @param pageable 페이징
     * @return 조회된 신고글
     */
    @Operation(summary = "삭제되지 않은 신고글 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseReportDto.class))))
    @GetMapping
    public ResponseEntity<List<ResponseReportDto>> listOfNotDelete(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Report> reports = reportService.listOfNotDeleted(pageable);
        return getListResponseReportDto(pageable, reports);
    }

    /**
     * 삭제된 신고글 조회
     *
     * @param pageable 페이징
     * @return 조회된 신고글
     */
    @Operation(summary = "삭제된 신고글 리스트 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseReportDto.class))))
    @GetMapping("/del")
    public ResponseEntity<List<ResponseReportDto>> listOfDelete(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Report> reports = reportService.listOfDeleted(pageable);
        return getListResponseReportDto(pageable, reports);
    }

    /**
     * 전체 신고글 조회
     *
     * @param pageable 페이징
     * @return 조회된 신고글
     */
    @Operation(summary = "전체 신고글 조회")
    @Parameters(value = {
            @Parameter(name = "size", description = "한 페이지에 보여줄 갯수", example = "10", schema = @Schema(type = "int")),
            @Parameter(name = "page", description = "몇 번째 페이지를 보여주는지 정함", example = "0", schema = @Schema(type = "int")),
            @Parameter(name = "pageable", hidden = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseReportDto.class))))
    @GetMapping("/all")
    public ResponseEntity<List<ResponseReportDto>> allList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Report> reports = reportService.allList(pageable);
        return getListResponseReportDto(pageable, reports);
    }
}
