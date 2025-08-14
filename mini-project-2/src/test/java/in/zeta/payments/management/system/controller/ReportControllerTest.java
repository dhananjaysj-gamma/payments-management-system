package in.zeta.payments.management.system.controller;

import in.zeta.payments.management.system.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class ReportControllerTest {

    @Mock
    private ReportServiceImpl reportService;

    @InjectMocks
    private ReportController reportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateMonthlyReport() {
        int year = 2023;
        int month = 10;
        byte[] reportData = new byte[]{1, 2, 3};

        when(reportService.generateMonthlyReport(year, month)).thenReturn(reportData);

        ResponseEntity<byte[]> response = reportController.getMonthlyReport(year, month);

        assertEquals(reportData, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(reportService, times(1)).generateMonthlyReport(year, month);
    }

    @Test
    void testGenerateQuarterlyReport() {
        int year = 2023;
        int quarter = 3;
        byte[] reportData = new byte[]{4, 5, 6};

        when(reportService.generateQuarterlyReport(year, quarter)).thenReturn(reportData);

        ResponseEntity<byte[]> response = reportController.getQuarterlyReport(year, quarter);

        assertEquals(reportData, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(reportService, times(1)).generateQuarterlyReport(year, quarter);
    }

}