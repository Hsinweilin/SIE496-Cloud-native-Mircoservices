package com.optimagrowth.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optimagrowth.payment.model.Payment;
import com.optimagrowth.payment.service.PaymentService;

import java.util.List;


@RestController
@RequestMapping(value = "v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Create a new payment
    // Todo: after message queue is implemented, need to update order status
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody Payment payment) {
        Payment createdPayment = paymentService.createPayment(payment);

        if (createdPayment == null) {
            // If payment cannot be created (e.g., invalid payment details)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Invalid payment details or payment failed.");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(createdPayment);
    }

    // Update an existing payment status
    @PutMapping("/{paymentId}")
    public ResponseEntity<?> updatePayment(@PathVariable Long paymentId, @RequestParam String paymentStatus) {
        Payment updatedPayment = paymentService.updatePaymentStatus(paymentId, paymentStatus);

        if (updatedPayment == null) {
            // If the payment cannot be updated (due to not found or invalid status)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Payment not found or invalid payment status.");
        }

        return ResponseEntity.status(HttpStatus.OK)
                             .body(updatedPayment);
    }

    // Get payment by ID
    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPayment(@PathVariable Long paymentId) {
        Payment payment = paymentService.getPayment(paymentId);

        if (payment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");  // Payment not found
        }

        return ResponseEntity.status(HttpStatus.OK).body(payment);  // Return payment details
    }

    // Get payments by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getPaymentsByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPaymentsByUserId(userId));  // Return list of payments
    }

    // Delete a payment
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<String> deletePayment(@PathVariable Long paymentId) {
        String message = paymentService.deletePayment(paymentId);

        if (message.equals("Payment not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else if (message.equals("Payment successfully deleted")) {
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the payment");
        }
    }
}

