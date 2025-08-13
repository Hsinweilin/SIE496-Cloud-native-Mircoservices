package com.optimagrowth.payment.service;

import com.optimagrowth.payment.model.Payment;
import com.optimagrowth.payment.repository.PaymentRepository;
import com.optimagrowth.payment.events.source.SimpleSourceBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private SimpleSourceBean simpleSourceBean;
    @InjectMocks
    private PaymentService paymentService;
    private Payment testPayment;

    @BeforeEach
    void setUp() {
        testPayment = new Payment();
        testPayment.setPaymentId(1L);
        testPayment.setUserId(123L);
        testPayment.setOrderId(456L);
        testPayment.setAmount(100);
        testPayment.setPaymentStatus("pending");
        testPayment.setPaymentDate(LocalDateTime.now());
    }

    @Test
    @DisplayName("Create payment should save and publish event")
    void createPaymentShouldSaveAndPublish() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
        Payment result = paymentService.createPayment(testPayment);
        assertNotNull(result);
        verify(simpleSourceBean).publishPaymentChange(any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Get payment by ID should return payment")
    void getPaymentShouldReturnPayment() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));
        Payment result = paymentService.getPayment(1L);
        assertEquals(1L, result.getPaymentId());
    }

    @Test
    @DisplayName("Get payment by ID should return null if not found")
    void getPaymentShouldReturnNullIfNotFound() {
        when(paymentRepository.findById(2L)).thenReturn(Optional.empty());
        Payment result = paymentService.getPayment(2L);
        assertNull(result);
    }

    @Test
    @DisplayName("Update payment status should update and save")
    void updatePaymentStatusShouldUpdateAndSave() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
        Payment result = paymentService.updatePaymentStatus(1L, "completed");
        assertNotNull(result);
        assertEquals("completed", result.getPaymentStatus());
    }

    @Test
    @DisplayName("Update payment status should return null if not found")
    void updatePaymentStatusShouldReturnNullIfNotFound() {
        when(paymentRepository.findById(2L)).thenReturn(Optional.empty());
        Payment result = paymentService.updatePaymentStatus(2L, "completed");
        assertNull(result);
    }

    @Test
    @DisplayName("Delete payment should return success message")
    void deletePaymentShouldReturnSuccess() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));
        doNothing().when(paymentRepository).delete(testPayment);
        String result = paymentService.deletePayment(1L);
        assertEquals("Payment successfully deleted", result);
    }

    @Test
    @DisplayName("Delete payment should return not found message")
    void deletePaymentShouldReturnNotFound() {
        when(paymentRepository.findById(2L)).thenReturn(Optional.empty());
        String result = paymentService.deletePayment(2L);
        assertEquals("Payment not found", result);
    }
}
