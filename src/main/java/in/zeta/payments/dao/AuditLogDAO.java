package in.zeta.payments.dao;

import in.zeta.payments.models.AuditLog;

import java.util.List;

public interface AuditLogDAO {
     List<AuditLog> getAllLogs();
     List<AuditLog> getLogsByPaymentID(int paymentID);
}
