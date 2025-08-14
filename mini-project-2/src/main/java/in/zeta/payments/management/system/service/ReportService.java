package in.zeta.payments.management.system.service;

public interface ReportService {
    byte[] generateMonthlyReport(Integer year, Integer month);
    byte[] generateQuarterlyReport(Integer year, Integer quarter);
}
