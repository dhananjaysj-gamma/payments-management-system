package in.zeta.payments.service;

public interface ReportService {
    void generateMonthlyReport(int year, int month);
    void generateQuarterlyReport(int year, int quarter);
}
