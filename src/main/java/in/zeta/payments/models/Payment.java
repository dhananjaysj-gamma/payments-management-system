package in.zeta.payments.models;

import in.zeta.payments.enums.PaymentStatus;
import in.zeta.payments.enums.PaymentType;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Payment {
    private int paymentId;
    private double amount;
    private PaymentType paymentType;
    private LocalDate paymentDate;
    private PaymentStatus status;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int createdBy;
    private int categoryID;



    public Payment(int paymentId, double amount, PaymentType paymentType,
                   LocalDate paymentDate, PaymentStatus status, int createdBy, int categoryID) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentType = paymentType;
        this.paymentDate = paymentDate;
        this.status = status;
        this.createdBy = createdBy;
        this.categoryID=categoryID;
    }

    public Payment() {

    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
}
