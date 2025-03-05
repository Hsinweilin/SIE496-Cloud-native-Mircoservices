package com.optimagrowth.license.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter @ToString
@Entity
@Table(name="orders")
public class Order extends RepresentationModel<Order> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "orderId", nullable = false)
	private Long orderId;

	@Column(name = "userId")
	private Long userId; // store user ID

	@Column(name = "orderDate", nullable = false, updatable = false)
	private LocalDateTime orderDate; // Date and time of order

	@Column(name = "productName", nullable = false)
	private String productName;

	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	@Column(name = "totalPrice", nullable = false)
	private Integer totalPrice; // Total price for the order

	@Column(name = "orderStatus", nullable = false)
	private String orderStatus; // E.g., 'pending', 'completed', 'shipped'

	// PrePersist method to auto-set the orderDate to current timestamp
	@PrePersist
	public void prePersist() {
		if (this.orderDate == null) {
			this.orderDate = LocalDateTime.now();  // Set the orderDate to current date and time
		}
	}

	// Getter and Setter methods for each field
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
}

