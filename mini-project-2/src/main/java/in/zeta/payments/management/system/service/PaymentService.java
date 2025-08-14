package in.zeta.payments.management.system.service;


import in.zeta.payments.management.system.dto.PaymentResponse;
import in.zeta.payments.management.system.entity.Payment;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(Payment payment);
    List<Payment> getAllPayments();
    PaymentResponse getPaymentById(Long userID);
    PaymentResponse updatePayment(Long userID, Payment payment);
    void deletePayment(Long userID);
}
