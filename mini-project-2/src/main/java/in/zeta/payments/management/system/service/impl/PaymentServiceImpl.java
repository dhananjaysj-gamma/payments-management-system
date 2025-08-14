package in.zeta.payments.management.system.service.impl;

import in.zeta.payments.management.system.dto.PaymentResponse;
import in.zeta.payments.management.system.entity.Payment;
import in.zeta.payments.management.system.entity.User;
import in.zeta.payments.management.system.exception.PaymentNotFoundException;
import in.zeta.payments.management.system.mapper.PaymentResponseMapper;
import in.zeta.payments.management.system.repository.PaymentRepository;
import in.zeta.payments.management.system.security.CustomUserDetails;
import in.zeta.payments.management.system.service.PaymentService;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import static in.zeta.payments.management.system.mapper.PaymentResponseMapper.mapToPaymentResponse;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, EntityManager entityManager) {
        this.paymentRepository = paymentRepository;
        this.entityManager=entityManager;
    }

    @Override
    public PaymentResponse createPayment(Payment payment) {
        logger.info("Creating payment for amount: {}", payment.getAmount());
        Long authenticatedUserID = getAuthenticatedUserID();
        User creator = entityManager.getReference(User.class, authenticatedUserID);
        payment.setCreatedBy(creator);
        paymentRepository.save(payment);
        logger.info("Payment created successfully with ID: {}", payment.getPaymentID());
        return mapToPaymentResponse(payment);
    }

    @Override
    public List<Payment> getAllPayments() {
        logger.info("Fetching all payments");
        return paymentRepository.findAll();
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        logger.info("Fetching payment by ID: {}", id);
        return paymentRepository.findById(id)
                .map(PaymentResponseMapper::mapToPaymentResponse)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with ID: " + id));
    }

    @Override
    public PaymentResponse updatePayment(Long paymentID, Payment payment) {
        logger.info("Updating payment with ID: {}", paymentID);
        Payment existingPayment = paymentRepository.findById(paymentID)
                .orElseThrow(() ->{
            logger.error("Payment not found with ID: {}", paymentID);
            return new PaymentNotFoundException("Payment not found with ID: " + paymentID);
        });

        if (payment.getAmount() != null) {
            existingPayment.setAmount(payment.getAmount());
        }
        if(payment.getPaymentType() != null) {
            existingPayment.setPaymentType(payment.getPaymentType());
        }
        if(payment.getCategory() != null) {
            existingPayment.setCategory(payment.getCategory());
        }
        if (payment.getPaymentDate() != null) {
            existingPayment.setPaymentDate(payment.getPaymentDate());
        }
        if (payment.getStatus() != null) {
            existingPayment.setStatus(payment.getStatus());
        }
        paymentRepository.save(existingPayment);
        logger.info("Payment updated successfully with ID: {}", paymentID);
        return mapToPaymentResponse(existingPayment);
    }

    @Override
    public void deletePayment(Long paymentID) {
        logger.info("Deleting payment with ID: {}", paymentID);
        if (!paymentRepository.existsById(paymentID)) {
            logger.error("Payment not found with ID: {}", paymentID);
            throw new PaymentNotFoundException("Payment not found with ID: " + paymentID);
        }
        paymentRepository.deleteById(paymentID);
        logger.info("Payment deleted successfully with ID: {}", paymentID);
    }

    private Long getAuthenticatedUserID() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((CustomUserDetails) authentication.getPrincipal()).getUserID();
    }

}

