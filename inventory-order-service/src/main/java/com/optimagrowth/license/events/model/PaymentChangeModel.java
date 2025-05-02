package com.optimagrowth.license.events.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;


@Getter @Setter @ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentChangeModel  implements Serializable{

	@JsonProperty("paymentId")
    private Long paymentId;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("paymentDate")
    private LocalDateTime paymentDate;

    @JsonProperty("orderId")
    private Long orderId;

    // Optional: remove this field if you decide it's not needed
    @JsonProperty("paymentStatus")
    private String paymentStatus;

    // Required no-args constructor for Jackson
    public PaymentChangeModel() {
    }

    public PaymentChangeModel(Long paymentId, Long userId, LocalDateTime paymentDate, Long orderId, String paymentStatus) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.paymentDate = paymentDate;
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
    }

    // Optional: remove this constructor if you remove paymentStatus
    public PaymentChangeModel(Long paymentId, Long userId, LocalDateTime paymentDate, Long orderId) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.paymentDate = paymentDate;
        this.orderId = orderId;
    }

    public Long getPaymentId() { return paymentId; }
	public Long getUserId() { return userId; }
	public LocalDateTime getPaymentDate() { return paymentDate; }
	public Long getOrderId() { return orderId; }
	public String getPaymentStatus() { return paymentStatus; }
}