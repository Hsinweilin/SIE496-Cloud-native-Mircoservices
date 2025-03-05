package com.optimagrowth.license.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Getter @Setter @ToString
@Entity
@Table(name="inventory")
public class Inventory extends RepresentationModel<Inventory> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  // Database will auto-increment the ID
	@Column(name = "inventoryId", nullable = false)
	private Long inventoryId;

	@Column(name = "productName", nullable = false)
	private String productName;

	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	@Column(name = "price", nullable = false)
	private Integer price;

	@Column(name = "description")
	private String description;

	// Getter and Setter for inventoryId
	public Long getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}

	// Getter and Setter for productName
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	// Getter and Setter for quantity
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	// Getter and Setter for price
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	// Getter and Setter for description
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
