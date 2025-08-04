package in.zeta.payments.dao.impl;

import in.zeta.payments.config.DBConnection;
import in.zeta.payments.dao.AuditLogDAO;
import in.zeta.payments.enums.PaymentStatus;
import in.zeta.payments.models.AuditLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDAOImpl implements AuditLogDAO {

    @Override
    public List<AuditLog> getAllLogs(){
        List<AuditLog> logs = new ArrayList<>();
        String query = "SELECT * FROM audit_log";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                logs.add(mapToAuditLog(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }

    @Override
    public List<AuditLog> getLogsByPaymentID(int paymentID) {
        List<AuditLog> logs = new ArrayList<>();
        String query = "SELECT * FROM audit_log WHERE payment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, paymentID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    logs.add(mapToAuditLog(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }

    private AuditLog mapToAuditLog(ResultSet rs) throws SQLException {
        AuditLog log = new AuditLog();
        log.setLogId(rs.getInt("log_id"));
        log.setOperation(rs.getString("operation"));
        log.setAmount(rs.getDouble("amount"));
        log.setTimestamp(Timestamp.valueOf(rs.getTimestamp("timestamp").toLocalDateTime()));
        log.setStatus(PaymentStatus.valueOf(rs.getString("status")));
        log.setPaymentId(rs.getInt("payment_id"));
        log.setUserId(rs.getInt("user_id"));
        return log;
    }
}
