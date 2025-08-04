package in.zeta.payments.service.impl;

import in.zeta.payments.dao.AuditLogDAO;
import in.zeta.payments.models.AuditLog;

import java.util.List;

public class AuditLogServiceImpl  implements AuditLogDAO {
    private final AuditLogDAO auditLogDAO;

    public AuditLogServiceImpl(AuditLogDAO auditLogDAO) {
        this.auditLogDAO = auditLogDAO;
    }

    @Override
    public List<AuditLog> getAllLogs() {
        List<AuditLog> logs = auditLogDAO.getAllLogs();
        if (logs.isEmpty()) {
            System.out.println("No audit logs found.");
        } else {
            System.out.println("=== Audit Trail ===");
            for (AuditLog log : logs) {
                System.out.printf(
                        "LogID: %d | Operation: %s | Amount: %.2f | Status: %s | Type: %s | Date: %s | PaymentID: %d | UserID: %d | Timestamp: %s%n",
                        log.getLogId(),
                        log.getOperation(),
                        log.getAmount(),
                        log.getStatus(),
                        log.getPaymentType(),
                        log.getPaymentDate(),
                        log.getPaymentId(),
                        log.getUserId(),
                        log.getTimestamp()
                );
            }
        }
        return logs;
    }

    @Override
    public List<AuditLog> getLogsByPaymentID(int paymentID) {
        List<AuditLog> logs = auditLogDAO.getLogsByPaymentID(paymentID);
        if (logs.isEmpty()) {
            System.out.println("No audit logs found for Payment ID: " + paymentID);
        } else {
            System.out.println("=== Audit Trail for Payment ID: " + paymentID + " ===");
            for (AuditLog log : logs) {
                System.out.printf(
                        "LogID: %d | Operation: %s | Amount: %.2f | Status: %s | Type: %s | Date: %s | PaymentID: %d | UserID: %d | Timestamp: %s%n",
                        log.getLogId(),
                        log.getOperation(),
                        log.getAmount(),
                        log.getStatus(),
                        log.getPaymentType(),
                        log.getPaymentDate(),
                        log.getPaymentId(),
                        log.getUserId(),
                        log.getTimestamp()
                );
            }
        }
        return logs;
    }
}
