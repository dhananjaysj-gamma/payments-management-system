package in.zeta.payments.service.impl;

import in.zeta.payments.dao.PaymentDAO;
import in.zeta.payments.dao.impl.PaymentDAOImpl;
import in.zeta.payments.enums.PaymentStatus;
import in.zeta.payments.exceptions.PaymentNotFoundException;
import in.zeta.payments.models.Payment;
import in.zeta.payments.service.PaymentService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class PaymentServiceImpl implements PaymentService {

    private final PaymentDAO paymentDAO = new PaymentDAOImpl();

    public PaymentServiceImpl(PaymentDAOImpl paymentDAO) {
    }

    public void addPayment(Payment payment) {
        payment.setAmount(payment.getAmount());
        payment.setPaymentType(payment.getPaymentType());
        payment.setPaymentDate(payment.getPaymentDate());
        payment.setStatus(payment.getStatus());
        payment.setCategoryID(payment.getCategoryID());
        payment.setCreatedAt(Timestamp.from(Instant.now()));
        payment.setUpdatedAt(Timestamp.from(Instant.now()));
        payment.setCreatedBy(payment.getCreatedBy());
        payment.setDescription(payment.getDescription());

        paymentDAO.save(payment);
    }

    public void updatePaymentStatus(int paymentId, String status) {
        Payment existing = paymentDAO.findById(paymentId);
        if (existing != null) {
            existing.setStatus(PaymentStatus.valueOf(status));
            existing.setUpdatedAt(Timestamp.from(Instant.now()));
            paymentDAO.update(existing);
            System.out.println("Payment updated successfully.");
        } else {
            throw new PaymentNotFoundException("Payment Not Found with ID " + paymentId);
        }
    }

    public List<Payment> getAllPaymentsByUser(int userId) {
        return paymentDAO.findAllByUserId(userId);
    }
}
