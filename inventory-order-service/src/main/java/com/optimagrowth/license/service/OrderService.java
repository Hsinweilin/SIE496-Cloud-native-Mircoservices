package com.optimagrowth.license.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.optimagrowth.license.model.Order;
import com.optimagrowth.license.repository.OrderRepository;
import com.optimagrowth.license.model.Inventory;
import com.optimagrowth.license.repository.InventoryRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    // Create a new order
    public Order createOrder(Order order) {

        Inventory inventory = inventoryRepository.findByProductName(order.getProductName());
        if (inventory == null || inventory.getQuantity() < order.getQuantity()) {
            return null;  // Not enough inventory
        }
        inventory.setQuantity(inventory.getQuantity() - order.getQuantity());  // Update the inventory quantity
        inventoryRepository.save(inventory);  // Save the updated inventory

        // Calculate the total price of the order
        Integer totalPrice = inventory.getPrice() * order.getQuantity();
        order.setTotalPrice(totalPrice);  // Set the total price on the order

        return orderRepository.save(order);  // Save the order to the repository
    }

    // Update an existing order, not allowing user to update order
    public Order updateOrderStatus(Long orderId, String orderStatus) {
        // Fetch the existing order by ID
        Order existingOrder = orderRepository.findById(orderId).orElse(null);

        // If the order is not found, return null
        if (existingOrder == null) {
            return null;  // Return null if order not found
        }

        existingOrder.setOrderStatus(orderStatus);

        // Save the updated order to the repository
        return orderRepository.save(existingOrder);  // Return the updated order
    }

    // Get order by ID
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    // Delete an order
    public String deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return "Order not found";  // Return a message instead of HTTP status
        }
        // Delete the order if it exists
        orderRepository.delete(order);
        return "Order successfully deleted";        
    }
}

