package in.zeta.payments.management.system.service.impl;

import in.zeta.payments.management.system.entity.Payment;
import in.zeta.payments.management.system.enums.Category;
import in.zeta.payments.management.system.enums.PaymentType;
import in.zeta.payments.management.system.exception.PdfReportGenerationException;
import in.zeta.payments.management.system.repository.PaymentRepository;
import in.zeta.payments.management.system.service.ReportService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {


    private final PaymentRepository paymentRepository;

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    public ReportServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public byte[] generateMonthlyReport(Integer year, Integer month) {
        logger.info("Generating monthly report for year: {}, month: {}", year, month);
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return generateReport(startDate, endDate, "Monthly Report");
    }

    @Override
    public byte[] generateQuarterlyReport(Integer year, Integer quarter) {
        logger.info("Generating quarterly report for year: {}, quarter: {}", year, quarter);
        int startMonth = (quarter - 1) * 3 + 1;
        LocalDate startDate = LocalDate.of(year, startMonth, 1);
        LocalDate endDate = startDate.plusMonths(2).withDayOfMonth(startDate.plusMonths(2).lengthOfMonth());
        return generateReport(startDate, endDate, "Quarterly Report");
    }

    private byte[] generateReport(LocalDate startDate, LocalDate endDate, String title) {
        logger.info("Generating report for period: {} to {}", startDate, endDate);
        List<Payment> payments = paymentRepository.findByPaymentDateBetween(startDate, endDate);

        BigDecimal totalAmount = payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<Category, Long> categoryCount = payments.stream()
                .collect(Collectors.groupingBy(Payment::getCategory, Collectors.counting()));

        Map<PaymentType, Long> paymentTypeCount = payments.stream()
                .collect(Collectors.groupingBy(Payment::getPaymentType, Collectors.counting()));

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.setLeading(16f);
                contentStream.newLineAtOffset(50, 700);


                contentStream.showText(title);
                contentStream.newLine();
                contentStream.showText("Period: " + startDate + " to " + endDate);
                contentStream.newLine();
                contentStream.showText("Total Amount: " + totalAmount);
                contentStream.newLine();


                contentStream.showText("Category Breakdown:");
                contentStream.newLine();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                for (Map.Entry<Category, Long> entry : categoryCount.entrySet()) {
                    contentStream.showText(" - " + entry.getKey() + ": " + entry.getValue());
                    contentStream.newLine();
                }


                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLine();
                contentStream.showText("Payment Type Breakdown:");
                contentStream.newLine();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                for (Map.Entry<PaymentType, Long> entry : paymentTypeCount.entrySet()) {
                    contentStream.showText(" - " + entry.getKey() + ": " + entry.getValue());
                    contentStream.newLine();
                }

                contentStream.endText();
            }
            document.save(outputStream);
            logger.info("Report generated successfully for period: {} to {}", startDate, endDate);
            return outputStream.toByteArray();

        } catch (IOException exception) {
            throw new PdfReportGenerationException("Error generating PDF report");
        }
    }


}
