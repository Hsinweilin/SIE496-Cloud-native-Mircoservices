package com.optimagrowth.license.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@Entity
@Table(name="licenses")
public class License extends RepresentationModel<License> {

	@Id
	@Column(name = "license_id", nullable = false)
	private String licenseId;
	private String description;
	@Column(name = "organization_id", nullable = false)
	private String organizationId;
	@Column(name = "product_name", nullable = false)
	private String productName;
	@Column(name = "license_type", nullable = false)
	private String licenseType;
	@Column(name="comment")
	private String comment;

	public License withComment(String comment){
		this.setComment(comment);
		return this;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}


	// Getter for licenseId
	public String getLicenseId() {
		return licenseId;
	}

	// Setter for licenseId
	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}

	// Getter for description
	public String getDescription() {
		return description;
	}

	// Setter for description
	public void setDescription(String description) {
		this.description = description;
	}

	// Getter for organizationId
	public String getOrganizationId() {
		return organizationId;
	}

	// Setter for organizationId
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	// Getter for productName
	public String getProductName() {
		return productName;
	}

	// Setter for productName
	public void setProductName(String productName) {
		this.productName = productName;
	}

	// Getter for licenseType
	public String getLicenseType() {
		return licenseType;
	}

	// Setter for licenseType
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	// Override toString() method (from Lombok @ToString)
	@Override
	public String toString() {
		return "License{" +
				", licenseId='" + licenseId + '\'' +
				", description='" + description + '\'' +
				", organizationId='" + organizationId + '\'' +
				", productName='" + productName + '\'' +
				", licenseType='" + licenseType + '\'' +
				'}';
	}

}