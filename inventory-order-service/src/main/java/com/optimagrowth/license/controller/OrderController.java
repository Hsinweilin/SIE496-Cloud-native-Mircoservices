package com.optimagrowth.license.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;

import com.optimagrowth.license.model.Order;
import com.optimagrowth.license.service.OrderService;

@RestController
@RequestMapping(value="v1/order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	// Create a new order
	@PostMapping
	public ResponseEntity<?> createOrder(@RequestBody Order order) {
		Order createdOrder = orderService.createOrder(order);

		if (createdOrder == null) {
			// If the order cannot be created (due to lack of inventory)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								 .body("Not enough inventory or invalid product.");
		}

		return ResponseEntity.status(HttpStatus.CREATED)
							 .body(createdOrder);
	}

	// Update an existing order
	@PutMapping("/{orderId}")
	public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @RequestParam String orderStatus) {
		Order updatedOrder = orderService.updateOrderStatus(orderId, orderStatus);

		if (updatedOrder == null) {
			// If the order cannot be updated (due to lack of inventory or not found)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								 .body("Order not found or not enough inventory.");
		}

		return ResponseEntity.status(HttpStatus.OK)
							 .body(updatedOrder);
	}

	// Get order by ID
	@GetMapping("/{orderId}")
	public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
		Order order = orderService.getOrder(orderId);

		if (order == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");  // Order not found
		}

		return ResponseEntity.status(HttpStatus.OK).body(order);  // Return order details
	}

	// Delete an order
	@DeleteMapping("/{orderId}")
	public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
		String message = orderService.deleteOrder(orderId);

		if (message.equals("Order not found")) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		}
		else if (message.equals("Order successfully deleted")) {
			return ResponseEntity.status(HttpStatus.OK).body(message);
		}
		else{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the order");
		}
	}
}