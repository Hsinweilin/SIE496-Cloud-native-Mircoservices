package com.optimagrowth.payment.repository;

import com.optimagrowth.payment.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;

    private Payment testPayment;

    @BeforeEach
    void setUp() {
        testPayment = new Payment();
        testPayment.setUserId(123L);
        testPayment.setOrderId(456L);
        testPayment.setAmount(100);
        testPayment.setPaymentStatus("pending");
        testPayment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(testPayment);
    }

    @Test
    @DisplayName("Find payment by orderId")
    void findByOrderIdShouldReturnPayment() {
        Payment found = paymentRepository.findByOrderId(456L);
        assertNotNull(found, "Should find payment by orderId");
        assertEquals(123L, found.getUserId());
    }

    @Test
    @DisplayName("Find payments by userId")
    void findByUserIdShouldReturnPayments() {
        List<Payment> payments = paymentRepository.findByUserId(123L);
        assertFalse(payments.isEmpty(), "Should find payments by userId");
        assertEquals(456L, payments.get(0).getOrderId());
    }

    @Test
    @DisplayName("Return null for non-existent orderId")
    void findByOrderIdShouldReturnNullIfNotFound() {
        Payment found = paymentRepository.findByOrderId(999L);
        assertNull(found, "Should return null if payment not found");
    }
}
