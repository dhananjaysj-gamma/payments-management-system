package in.zeta.payments.management.system.service;

import in.zeta.payments.management.system.entity.Payment;
import in.zeta.payments.management.system.repository.PaymentRepository;
import in.zeta.payments.management.system.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static in.zeta.payments.management.system.factory.PaymentFactory.getPayment;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReportServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateMonthlyReport_Success() {
        LocalDate startDate = LocalDate.of(2023, 10, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        List<Payment> payments = Arrays.asList(getPayment());

        when(paymentRepository.findByPaymentDateBetween(startDate, endDate)).thenReturn(payments);

        byte[] report = reportService.generateMonthlyReport(2023, 10);

        assertNotNull(report);
        assertTrue(report.length > 0);
        verify(paymentRepository, times(1)).findByPaymentDateBetween(startDate, endDate);
    }

    @Test
    void testGenerateQuarterlyReport_Success() {
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = startDate.plusMonths(2).withDayOfMonth(startDate.plusMonths(2).lengthOfMonth());
        List<Payment> payments = Arrays.asList(getPayment());

        when(paymentRepository.findByPaymentDateBetween(startDate, endDate)).thenReturn(payments);

        byte[] report = reportService.generateQuarterlyReport(2023, 3);

        assertNotNull(report);
        assertTrue(report.length > 0);
        verify(paymentRepository, times(1)).findByPaymentDateBetween(startDate, endDate);
    }

}