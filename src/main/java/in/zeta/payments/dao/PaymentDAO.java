package in.zeta.payments.dao;

import in.zeta.payments.models.Payment;

import java.util.List;

public interface PaymentDAO {
    void save(Payment payment);
    Payment findById(int paymentId);
    List<Payment> findAllByUserId(int userId);
    List<Payment> findAll();
    void update(Payment payment);
    void delete(int paymentId);
}
