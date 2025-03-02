package com.optimagrowth.license.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@Entity
@Table(name="users")
public class User extends RepresentationModel<User> {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Database will auto-increment the ID
	@Column(name = "userId", nullable = false)
	private Long userId;
	@Column(name = "username", nullable = false)
	private String username;
	@Column(name = "email", nullable = false)
	private String email;
	@Column(name = "password", nullable = false)
	private String password;

	    // Getter and Setter for userId
		public Long getUserId() {
			return userId;
		}
	
		public void setUserId(Long userId) {
			this.userId = userId;
		}
	
		// Getter and Setter for username
		public String getUsername() {
			return username;
		}
	
		public void setUsername(String username) {
			this.username = username;
		}
	
		// Getter and Setter for email
		public String getEmail() {
			return email;
		}
	
		public void setEmail(String email) {
			this.email = email;
		}
	
		// Getter and Setter for password
		public String getPassword() {
			return password;
		}
	
		public void setPassword(String password) {
			this.password = password;
		}
}

