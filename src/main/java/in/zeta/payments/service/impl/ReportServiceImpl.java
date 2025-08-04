package in.zeta.payments.service.impl;

import in.zeta.payments.dao.PaymentDAO;
import in.zeta.payments.enums.PaymentStatus;
import in.zeta.payments.models.Payment;
import in.zeta.payments.service.ReportService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportServiceImpl implements ReportService {
    private final PaymentDAO paymentDAO;

    public ReportServiceImpl(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    @Override
    public void generateMonthlyReport(int year, int month) {
        List<Payment> allPayments = paymentDAO.findAll();

        List<Payment> monthlyPayments = allPayments.stream()
                .filter(p -> p.getPaymentDate().getYear() == year && p.getPaymentDate().getMonthValue() == month)
                .collect(Collectors.toList());

        printSummary("Monthly Report", monthlyPayments);
    }

    @Override
    public void generateQuarterlyReport(int year, int quarter) {
        int startMonth = (quarter - 1) * 3 + 1;
        int endMonth = startMonth + 2;

        List<Payment> allPayments = paymentDAO.findAll();

        List<Payment> quarterlyPayments = allPayments.stream()
                .filter(p -> {
                    int month = p.getPaymentDate().getMonthValue();
                    return p.getPaymentDate().getYear() == year && month >= startMonth && month <= endMonth;
                })
                .collect(Collectors.toList());

        printSummary("Quarterly Report", quarterlyPayments);
    }

    private void printSummary(String title, List<Payment> payments) {
        System.out.println("==== " + title + " ====");
        double totalAmount = payments.stream().mapToDouble(Payment::getAmount).sum();
        Map<PaymentStatus, Long> statusCounts = payments.stream()
                .collect(Collectors.groupingBy(Payment::getStatus, Collectors.counting()));

        System.out.println("Total Payments: " + payments.size());
        System.out.println("Total Amount: ₹" + totalAmount);
        System.out.println("Status Breakdown: " + statusCounts);
        System.out.println("=========================");
    }
}
