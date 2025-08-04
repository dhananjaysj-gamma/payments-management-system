package in.zeta.payments.models;

import in.zeta.payments.enums.PaymentStatus;
import in.zeta.payments.enums.PaymentType;
import java.sql.Timestamp;
import java.time.LocalDate;

public class AuditLog {
    private int logId;
    private String operation;
    private double amount;
    private Timestamp timestamp;
    private PaymentType paymentType;
    private LocalDate paymentDate;
    private PaymentStatus status;
    private int paymentId;
    private int userId;

    public AuditLog(int logId, String operation, double amount, Timestamp timestamp, PaymentStatus status, int paymentId, int userId) {
        this.logId = logId;
        this.operation = operation;
        this.amount = amount;
        this.timestamp = timestamp;
        this.status = status;
        this.paymentId = paymentId;
        this.userId = userId;
    }

    public AuditLog() {}

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }
}

