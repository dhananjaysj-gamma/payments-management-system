package in.zeta.payments.management.system.controller;

import in.zeta.payments.management.system.dto.PaymentResponse;
import in.zeta.payments.management.system.entity.Payment;
import in.zeta.payments.management.system.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static in.zeta.payments.management.system.constant.PaymentsManagementSystemRoutes.BASE_URL;
import static in.zeta.payments.management.system.constant.PaymentsManagementSystemRoutes.PaymentRoutes.*;


@RestController
@RequestMapping(value = BASE_URL)
public class PaymentController {

    private final PaymentService paymentService;
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasRole('FINANCE_MANAGER')")
    @PostMapping(value = CREATE_PAYMENT_URL)
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody Payment payment) {
        logger.info("Creating payment for amount: {}", payment.getAmount());
        PaymentResponse savedPayment = paymentService.createPayment(payment);
        return ResponseEntity.ok(savedPayment);
    }

    @PreAuthorize("hasRole('VIEWER')")
    @GetMapping(value = GET_ALL_PAYMENTS_URL)
    public ResponseEntity<List<Payment>> getAllPayments() {
        logger.info("Fetching all payments");
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @PreAuthorize("hasRole('VIEWER')")
    @GetMapping(value = GET_PAYMENT_BY_ID_URL)
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long paymentID) {
        logger.info("Fetching payment with ID: {}", paymentID);
        return ResponseEntity.ok(paymentService.getPaymentById(paymentID));
    }

    @PreAuthorize("hasRole('FINANCE_MANAGER')")
    @PutMapping(value = UPDATE_PAYMENT_URL)
    public ResponseEntity<PaymentResponse> updatePayment(@PathVariable Long paymentID, @RequestBody Payment payment) {
        logger.info("Updating payment with ID: {}", paymentID);
        return ResponseEntity.ok(paymentService.updatePayment(paymentID, payment));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = DELETE_PAYMENT_URL)
    public ResponseEntity<Void> deletePayment(@PathVariable Long paymentID) {
        logger.info("Deleting payment with ID: {}", paymentID);
        paymentService.deletePayment(paymentID);
        logger.info("Payment deleted successfully with ID: {}", paymentID);
        return ResponseEntity.noContent().build();
    }
}
