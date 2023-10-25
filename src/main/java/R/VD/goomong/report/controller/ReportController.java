package R.VD.goomong.report.controller;

import R.VD.goomong.report.dto.request.RequestReportDto;
import R.VD.goomong.report.dto.response.ResponseReportDto;
import R.VD.goomong.report.model.Report;
import R.VD.goomong.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    /**
     * 신고글 생성
     *
     * @param requestReportDto - 신고글 생성 request
     * @param filesList        - 신고글 파일 업로드
     * @return - 생성 완료 시 200
     */
    @PostMapping("/report")
    public ResponseEntity<Object> initReport(@Validated @ModelAttribute RequestReportDto requestReportDto, @RequestParam(required = false) MultipartFile[] filesList) {
        reportService.saveReport(requestReportDto, filesList);
        return ResponseEntity.ok().build();
    }

    /**
     * 신고글 수정
     *
     * @param reportId         - 수정할 신고글 pk
     * @param requestReportDto - 수정할 내용
     * @param filesList        - 수정할 업로드 파일
     * @return - 수정된 신고글
     */
    @PutMapping("/report/{reportId}")
    public ResponseEntity<ResponseReportDto> updateReport(@PathVariable Long reportId, @Validated @ModelAttribute RequestReportDto requestReportDto, @RequestParam(required = false) MultipartFile[] filesList) {
        Report report = reportService.updateReport(reportId, requestReportDto, filesList);
        return ResponseEntity.ok(report.toResponseReportDto());
    }
}
