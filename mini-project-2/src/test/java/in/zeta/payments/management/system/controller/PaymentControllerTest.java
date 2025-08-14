package in.zeta.payments.management.system.controller;

import in.zeta.payments.management.system.dto.PaymentResponse;
import in.zeta.payments.management.system.entity.Payment;
import in.zeta.payments.management.system.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static in.zeta.payments.management.system.factory.PaymentFactory.PAYMENT_ID;
import static in.zeta.payments.management.system.factory.PaymentFactory.getPayment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static in.zeta.payments.management.system.mapper.PaymentResponseMapper.mapToPaymentResponse;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePayment() {
        Payment payment = getPayment();
        PaymentResponse paymentResponse = mapToPaymentResponse(payment);
        when(paymentService.createPayment(payment)).thenReturn(paymentResponse);

        ResponseEntity<PaymentResponse> response = paymentController.createPayment(payment);

        assertEquals(paymentResponse, response.getBody());
        verify(paymentService, times(1)).createPayment(payment);
    }

    @Test
    void testGetAllPayments() {
        Payment payment = getPayment();
        List<Payment> payments = Arrays.asList(payment);
        when(paymentService.getAllPayments()).thenReturn(payments);

        ResponseEntity<List<Payment>> response = paymentController.getAllPayments();

        assertEquals(payments, response.getBody());
        verify(paymentService, times(1)).getAllPayments();
    }

    @Test
    void testGetPaymentById() {
        Payment payment = getPayment();
        PaymentResponse paymentResponse = mapToPaymentResponse(payment);
        when(paymentService.getPaymentById(PAYMENT_ID)).thenReturn(paymentResponse);

        ResponseEntity<PaymentResponse> response = paymentController.getPaymentById(PAYMENT_ID);

        assertEquals(paymentResponse, response.getBody());
        verify(paymentService, times(1)).getPaymentById(PAYMENT_ID);
    }

    @Test
    void testUpdatePayment() {
        Payment payment = getPayment();
        PaymentResponse paymentResponse = mapToPaymentResponse(payment);
        when(paymentService.updatePayment(PAYMENT_ID, payment)).thenReturn(paymentResponse);

        ResponseEntity<PaymentResponse> response = paymentController.updatePayment(PAYMENT_ID, payment);

        assertEquals(paymentResponse, response.getBody());
        verify(paymentService, times(1)).updatePayment(PAYMENT_ID, payment);
    }

    @Test
    void testDeletePayment() {
        ResponseEntity<Void> response = paymentController.deletePayment(PAYMENT_ID);

        assertEquals(204, response.getStatusCodeValue());
        verify(paymentService, times(1)).deletePayment(PAYMENT_ID);
    }
}