package com.optimagrowth.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.optimagrowth.payment.model.Payment;
import com.optimagrowth.payment.service.PaymentService;
import javax.annotation.security.RolesAllowed;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.List;

//import usercontex
import com.optimagrowth.payment.utils.UserContext;
import com.optimagrowth.payment.utils.UserContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Qualifier;




@RestController
@RequestMapping(value = "v1/payment")
public class PaymentController {  

    @Autowired
    @Qualifier("getRestTemplate") // or "keycloakRestTemplate"
    private RestTemplate restTemplate;

    @Autowired
    private PaymentService paymentService;

    @Value("${gateway.url}")
    private String gatewayUrl;

    // Create a new payment
    @RolesAllowed("USER")
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody Payment payment) {
        Payment createdPayment = paymentService.createPayment(payment);
    
        if (createdPayment == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Invalid payment details or payment failed.");
        }
    
        // ✅ No more HTTP call to order service here — handled by message queue via SimpleSourceBean
    
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(createdPayment);
    }
    // Todo: after message queue is implemented, need to update order status
    // @RolesAllowed("USER") 
    // @PostMapping
    // public ResponseEntity<?> createPayment(@RequestBody Payment payment) {
    //     Payment createdPayment = paymentService.createPayment(payment);

    //     if (createdPayment == null) {
    //         // If payment cannot be created (e.g., invalid payment details)
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    //                              .body("Invalid payment details or payment failed.");
    //     }
        
    //     String authHeader = UserContextHolder.getContext().getAuthToken();

    //     // Call Order Service to update status
    //     try {
    //         String orderId = String.valueOf(payment.getOrderId());
    //         String url = "http://inventory-order-service/v1/order/" + orderId + "?orderStatus=paid";
    
    //         // Prepare headers
    //         HttpHeaders headers = new HttpHeaders();
    //         String authToken = UserContextHolder.getContext().getAuthToken();
    //         String correlationId = UserContextHolder.getContext().getCorrelationId();
        
    //         if (authToken != null && !authToken.isEmpty()) {
    //             headers.set(UserContext.AUTH_TOKEN, authToken);
    //         }
        
    //         if (correlationId != null && !correlationId.isEmpty()) {
    //             headers.set(UserContext.CORRELATION_ID, correlationId);
    //         }

    //         // System.out.println("Url: " + url);
    //         // System.out.println("Token: " + authToken);

    //         headers.set("Authorization", authToken);
        
    //         HttpEntity<Void> entity = new HttpEntity<>(headers);
        
    //         restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
    
    //     } catch (Exception e) {
    //         e.printStackTrace(); // full console trace
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                      .body("Failed to contact inventory-order-service: " + e.getClass().getSimpleName() + " - " + e.getMessage());
    //     }

    //     return ResponseEntity.status(HttpStatus.CREATED)
    //                          .body(createdPayment);
    // }

    // Update an existing payment status
    @RolesAllowed("ADMIN") 
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
    @RolesAllowed({ "ADMIN", "USER" }) 
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
    @RolesAllowed("ADMIN") 
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

