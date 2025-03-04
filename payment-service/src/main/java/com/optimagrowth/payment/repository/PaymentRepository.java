package com.optimagrowth.payment.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.optimagrowth.payment.model.Payment;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {
    // Find a specific payment by orderId
    public Payment findByOrderId(Long orderId);
    // Find all payments by userId
    public List<Payment> findByUserId(Long userId);
}

