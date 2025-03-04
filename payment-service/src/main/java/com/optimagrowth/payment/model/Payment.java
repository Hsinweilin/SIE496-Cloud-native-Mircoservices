package com.optimagrowth.payment.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter @ToString
@Entity
@Table(name="payment")
public class Payment extends RepresentationModel<Payment> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentId", nullable = false)
    private Long paymentId;

    @Column(name = "userId")
    private Long userId; // store user ID

    @Column(name = "paymentDate", nullable = false, updatable = false)
    private LocalDateTime paymentDate; // Date and time of payment

    @Column(name = "orderId", nullable = false)
    private Long orderId;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "paymentStatus", nullable = false)
    private String paymentStatus; // E.g., 'pending', 'completed', 'shipped' optional, can be deleted

    // PrePersist method to auto-set the orderDate to current timestamp
    @PrePersist
    public void prePersist() {
        if (this.paymentDate == null) {
            this.paymentDate = LocalDateTime.now();  // Set the paymentDate to current date and time
        }
    }

    // Getter and Setter for paymentId
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    // Getter and Setter for userId
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Getter and Setter for paymentDate
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    // Getter and Setter for orderId
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    // Getter and Setter for amount
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    // Getter and Setter for paymentStatus
    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}