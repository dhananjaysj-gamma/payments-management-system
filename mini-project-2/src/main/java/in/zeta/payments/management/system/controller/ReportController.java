package in.zeta.payments.management.system.controller;

import in.zeta.payments.management.system.service.impl.ReportServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static in.zeta.payments.management.system.constant.PaymentsManagementSystemRoutes.BASE_URL;
import static in.zeta.payments.management.system.constant.PaymentsManagementSystemRoutes.ReportGenerationRoutes.GENERATE_MONTHLY_REPORT_URL;
import static in.zeta.payments.management.system.constant.PaymentsManagementSystemRoutes.ReportGenerationRoutes.GENERATE_QUARTERLY_REPORT_URL;

@RestController
@RequestMapping(value = BASE_URL)

public class ReportController {

    private final ReportServiceImpl reportService;
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    public ReportController(ReportServiceImpl reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = GENERATE_MONTHLY_REPORT_URL)
    public ResponseEntity<byte[]> getMonthlyReport(
            @RequestParam Integer year,
            @RequestParam Integer month) {
        logger.info("Generating monthly report for year: {}, month: {}", year, month);
        byte[] pdfBytes = reportService.generateMonthlyReport(year, month);
        String fileName = "Monthly_Report_" + year + "_" + month + ".pdf";
        logger.info("Monthly report generated successfully for year: {}, month: {}", year, month);
        return buildReportResponse(pdfBytes, fileName);
    }

    @GetMapping(value = GENERATE_QUARTERLY_REPORT_URL)
    public ResponseEntity<byte[]> getQuarterlyReport(
            @RequestParam Integer year,
            @RequestParam Integer quarter) {
        logger.info("Generating quarterly report for year: {}, quarter: {}", year, quarter);
        byte[] pdfBytes = reportService.generateQuarterlyReport(year, quarter);
        String fileName = "Monthly_Report_" + year + "_" + quarter + ".pdf";
        logger.info("Quarterly report generated successfully for year: {}, quarter: {}", year, quarter);
        return buildReportResponse(pdfBytes, fileName);
    }

    private ResponseEntity<byte[]> buildReportResponse(byte[] pdfBytes, String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
