package R.VD.goomong.report.controller;

import R.VD.goomong.report.dto.request.RequestReportDto;
import R.VD.goomong.report.dto.response.ResponseReportDto;
import R.VD.goomong.report.model.Report;
import R.VD.goomong.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    private static ResponseEntity<List<ResponseReportDto>> getListResponseEntity(Page<Report> reports) {
        long totalElements = reports.getTotalElements();
        int totalPages = reports.getTotalPages();

        return ResponseEntity.ok()
                .header("TotalPages", String.valueOf(totalPages))
                .header("TotalData", String.valueOf(totalElements))
                .body(reports.getContent().stream().map(Report::toResponseReportDto).toList());
    }

    /**
     * 신고글 생성
     *
     * @param requestReportDto 신고글 생성 request
     * @param filesList        신고글 파일 업로드
     * @return 생성 완료 시 200
     */
    @PostMapping("/report")
    public ResponseEntity<Object> initReport(@Validated @ModelAttribute RequestReportDto requestReportDto, @RequestParam(required = false) MultipartFile[] filesList) {
        log.info("requestReportDto={}", requestReportDto);
        reportService.saveReport(requestReportDto, filesList);
        return ResponseEntity.ok().build();
    }

    /**
     * 신고글 수정
     *
     * @param reportId         수정할 신고글 pk
     * @param requestReportDto 수정할 내용
     * @param filesList        수정할 업로드 파일
     * @return 수정된 신고글
     */
    @PutMapping("/report/{reportId}")
    public ResponseEntity<ResponseReportDto> updateReport(@PathVariable Long reportId, @Validated @ModelAttribute RequestReportDto requestReportDto, @RequestParam(required = false) MultipartFile[] filesList) {
        log.info("reportId={}", reportId);
        log.info("requestReportDto={}", requestReportDto);
        Report report = reportService.updateReport(reportId, requestReportDto, filesList);
        return ResponseEntity.ok(report.toResponseReportDto());
    }

    /**
     * 신고글 소프트딜리트
     *
     * @param reportId 삭제할 신고글 pk
     * @return 삭제 완료 시 200
     */
    @PutMapping("/report/softdel/{reportId}")
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
    @GetMapping
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponseReportDto>> listOfNotDelete(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Report> reports = reportService.listOfNotDeleted(pageable);

        return getListResponseEntity(reports);
    }

    /**
     * 삭제된 신고글 조회
     *
     * @param pageable 페이징
     * @return 조회된 신고글
     */
    @GetMapping("/del")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponseReportDto>> listOfDelete(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Report> reports = reportService.listOfDeleted(pageable);

        return getListResponseEntity(reports);
    }

    /**
     * 전체 신고글 조회
     *
     * @param pageable 페이징
     * @return 조회된 신고글
     */
    @GetMapping("/all")
    @CrossOrigin(exposedHeaders = {"TotalPages", "TotalData"})
    public ResponseEntity<List<ResponseReportDto>> allList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Report> reports = reportService.allList(pageable);

        return getListResponseEntity(reports);
    }
}
