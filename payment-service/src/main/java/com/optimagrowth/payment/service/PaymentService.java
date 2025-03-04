package com.optimagrowth.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimagrowth.payment.model.Payment;
import com.optimagrowth.payment.repository.PaymentRepository;

import java.util.List;


@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    // Create a new payment
    public Payment createPayment(Payment payment) {
        // add additional business logic after message queue is implemented
        // check with order and the amount paid
        return paymentRepository.save(payment);  // Save the payment to the repository
    }

    // Update an existing payment's status (e.g., mark as completed or failed)
    public Payment updatePaymentStatus(Long paymentId, String paymentStatus) {
        // Fetch the existing payment by ID
        Payment existingPayment = paymentRepository.findById(paymentId).orElse(null);

        // If the payment is not found, return null
        if (existingPayment == null) {
            return null;  // Return null if payment not found
        }

        // Set the new payment status
        existingPayment.setPaymentStatus(paymentStatus);

        // Save the updated payment to the repository
        return paymentRepository.save(existingPayment);  // Return the updated payment
    }

    // Get payment by ID
    public Payment getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId).orElse(null);
    }

    // Get all payments by userId
    public List<Payment> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    // Delete a payment
    public String deletePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment == null) {
            return "Payment not found";  // Return a message instead of HTTP status
        }
        // Delete the payment if it exists
        paymentRepository.delete(payment);
        return "Payment successfully deleted";        
    }
}

