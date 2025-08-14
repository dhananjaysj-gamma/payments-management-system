package in.zeta.payments.management.system.service;

import in.zeta.payments.management.system.dto.PaymentResponse;
import in.zeta.payments.management.system.entity.Payment;
import in.zeta.payments.management.system.entity.User;
import in.zeta.payments.management.system.enums.Category;
import in.zeta.payments.management.system.enums.PaymentStatus;
import in.zeta.payments.management.system.exception.PaymentNotFoundException;
import in.zeta.payments.management.system.repository.PaymentRepository;
import in.zeta.payments.management.system.security.CustomUserDetails;
import in.zeta.payments.management.system.service.impl.PaymentServiceImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static in.zeta.payments.management.system.factory.PaymentFactory.getPayment;
import static in.zeta.payments.management.system.factory.PaymentFactory.PAYMENT_ID;
import static in.zeta.payments.management.system.factory.PaymentFactory.AMOUNT;
import static in.zeta.payments.management.system.factory.PaymentFactory.PAYMENT_TYPE_INCOMING;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    private void mockAuthenticatedUser(Long userID) {
        User user = new User();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        user.setUserID(userID);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
    }
    @Test
    void testCreatePayment_Success() {
        mockAuthenticatedUser(1L);
        Payment payment = getPayment();
        User user = new User();
        user.setUserID(1L);

        when(entityManager.getReference(User.class, 1L)).thenReturn(user);
        when(paymentRepository.save(payment)).thenReturn(payment);

        PaymentResponse response = paymentService.createPayment(payment);

        assertNotNull(response);
        verify(entityManager, times(1)).getReference(User.class, 1L);
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testGetAllPayments_Success() {
        List<Payment> payments = Arrays.asList(getPayment());
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(1, result.size());
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void testGetPaymentById_Success() {
        Payment payment = getPayment();
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(payment));

        PaymentResponse response = paymentService.getPaymentById(PAYMENT_ID);

        assertNotNull(response);
        verify(paymentRepository, times(1)).findById(PAYMENT_ID);
    }

    @Test
    void testGetPaymentById_NotFound() {
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class, () -> paymentService.getPaymentById(PAYMENT_ID));
        verify(paymentRepository, times(1)).findById(PAYMENT_ID);
    }

    @Test
    void testUpdatePayment_Success() {
        Payment existingPayment = getPayment();
        Payment updatedPayment = getPayment();

        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(existingPayment));
        when(paymentRepository.save(existingPayment)).thenReturn(updatedPayment);

        PaymentResponse response = paymentService.updatePayment(PAYMENT_ID, updatedPayment);

        assertNotNull(response);
        verify(paymentRepository, times(1)).findById(PAYMENT_ID);
        verify(paymentRepository, times(1)).save(existingPayment);
    }

    @Test
    void testUpdatePayment_NotFound() {
        Payment updatedPayment = getPayment();
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class, () -> paymentService.updatePayment(PAYMENT_ID, updatedPayment));
        verify(paymentRepository, times(1)).findById(PAYMENT_ID);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testUpdatePayment_UpdatesAmount_WhenAmountIsNotNull() {
        Payment existingPayment = getPayment();
        Payment updatedPayment = getPayment();

        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(existingPayment));
        when(paymentRepository.save(existingPayment)).thenReturn(existingPayment);

        paymentService.updatePayment(PAYMENT_ID, updatedPayment);

        assertEquals(AMOUNT, existingPayment.getAmount());
    }

    @Test
    void testUpdatePayment_UpdatesPaymentType_WhenPaymentTypeIsNotNull() {
        Payment existingPayment = getPayment();
        Payment updatedPayment = getPayment();

        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(existingPayment));
        when(paymentRepository.save(existingPayment)).thenReturn(existingPayment);

        paymentService.updatePayment(PAYMENT_ID, updatedPayment);

        assertEquals(PAYMENT_TYPE_INCOMING, existingPayment.getPaymentType());
    }

    @Test
    void testUpdatePayment_UpdatesCategory_WhenCategoryIsNotNull() {
        Payment existingPayment = getPayment();
        existingPayment.setCategory(null);
        Payment updatedPayment = getPayment();
        updatedPayment.setCategory(Category.SALARY_PAYMENT);

        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(existingPayment));
        when(paymentRepository.save(existingPayment)).thenReturn(existingPayment);

        paymentService.updatePayment(PAYMENT_ID, updatedPayment);

        assertEquals(Category.SALARY_PAYMENT, existingPayment.getCategory());
    }

    @Test
    void testUpdatePayment_UpdatesPaymentDate_WhenPaymentDateIsNotNull() {
        Payment existingPayment = getPayment();
        existingPayment.setPaymentDate(null);
        Payment updatedPayment = getPayment();
        LocalDate newDate = LocalDate.now();
        updatedPayment.setPaymentDate(newDate);

        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(existingPayment));
        when(paymentRepository.save(existingPayment)).thenReturn(existingPayment);

        paymentService.updatePayment(PAYMENT_ID, updatedPayment);

        assertEquals(newDate, existingPayment.getPaymentDate());
    }

    @Test
    void testUpdatePayment_UpdatesStatus_WhenStatusIsNotNull() {
        Payment existingPayment = getPayment();
        existingPayment.setStatus(null);
        Payment updatedPayment = getPayment();
        updatedPayment.setStatus(PaymentStatus.COMPLETED);

        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(existingPayment));
        when(paymentRepository.save(existingPayment)).thenReturn(existingPayment);

        paymentService.updatePayment(PAYMENT_ID, updatedPayment);

        assertEquals(PaymentStatus.COMPLETED, existingPayment.getStatus());
    }

    @Test
    void testDeletePayment_Success() {
        when(paymentRepository.existsById(PAYMENT_ID)).thenReturn(true);

        paymentService.deletePayment(PAYMENT_ID);

        verify(paymentRepository, times(1)).existsById(PAYMENT_ID);
        verify(paymentRepository, times(1)).deleteById(PAYMENT_ID);
    }

    @Test
    void testDeletePayment_NotFound() {
        when(paymentRepository.existsById(PAYMENT_ID)).thenReturn(false);

        assertThrows(PaymentNotFoundException.class, () -> paymentService.deletePayment(PAYMENT_ID));
        verify(paymentRepository, times(1)).existsById(PAYMENT_ID);
        verify(paymentRepository, never()).deleteById(PAYMENT_ID);
    }
}