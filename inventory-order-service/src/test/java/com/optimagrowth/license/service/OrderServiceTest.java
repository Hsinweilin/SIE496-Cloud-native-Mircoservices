package com.optimagrowth.license.service;

import com.optimagrowth.license.model.Inventory;
import com.optimagrowth.license.model.Order;
import com.optimagrowth.license.repository.InventoryRepository;
import com.optimagrowth.license.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setOrderId(1L);
        order.setUserId(1L);
        order.setProductName("Widget");
        order.setQuantity(5);
        order.setTotalPrice(500);
        order.setOrderStatus("pending");
        order.setOrderDate(LocalDateTime.now());

        inventory = new Inventory();
        inventory.setInventoryId(1L);
        inventory.setProductName("Widget");
        inventory.setQuantity(100);
        inventory.setPrice(100);
        inventory.setDescription("A sample widget");
    }

    @Test
    @DisplayName("createOrder should create and return order when inventory sufficient")
    void createOrderShouldCreateAndReturnOrderWhenInventorySufficient() {
        // Arrange
        when(inventoryRepository.findByProductName("Widget")).thenReturn(inventory);
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        Order result = orderService.createOrder(order);

        // Assert
        assertNotNull(result);
        assertEquals(order.getProductName(), result.getProductName());
        assertEquals(order.getQuantity(), result.getQuantity());
        assertEquals(500, result.getTotalPrice()); // 5 * 100
        verify(inventoryRepository).findByProductName("Widget");
        verify(inventoryRepository).save(any(Inventory.class));
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("createOrder should return null when inventory insufficient")
    void createOrderShouldReturnNullWhenInventoryInsufficient() {
        // Arrange
        inventory.setQuantity(3); // Less than order quantity (5)
        when(inventoryRepository.findByProductName("Widget")).thenReturn(inventory);

        // Act
        Order result = orderService.createOrder(order);

        // Assert
        assertNull(result);
        verify(inventoryRepository).findByProductName("Widget");
        verify(inventoryRepository, never()).save(any(Inventory.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("createOrder should return null when product not found")
    void createOrderShouldReturnNullWhenProductNotFound() {
        // Arrange
        when(inventoryRepository.findByProductName("Widget")).thenReturn(null);

        // Act
        Order result = orderService.createOrder(order);

        // Assert
        assertNull(result);
        verify(inventoryRepository).findByProductName("Widget");
        verify(inventoryRepository, never()).save(any(Inventory.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("getOrder should return order when found")
    void getOrderShouldReturnOrderWhenFound() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Act
        Order result = orderService.getOrder(1L);

        // Assert
        assertNotNull(result);
        assertEquals(order.getProductName(), result.getProductName());
        verify(orderRepository).findById(1L);
    }

    @Test
    @DisplayName("getOrder should return null when not found")
    void getOrderShouldReturnNullWhenNotFound() {
        // Arrange
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Order result = orderService.getOrder(999L);

        // Assert
        assertNull(result);
        verify(orderRepository).findById(999L);
    }

    @Test
    @DisplayName("updateOrderStatus should update status and return order when found")
    void updateOrderStatusShouldUpdateStatusAndReturnOrderWhenFound() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = orderService.updateOrderStatus(1L, "completed");

        // Assert
        assertNotNull(result);
        assertEquals("completed", result.getOrderStatus());
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("updateOrderStatus should return null when not found")
    void updateOrderStatusShouldReturnNullWhenNotFound() {
        // Arrange
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Order result = orderService.updateOrderStatus(999L, "completed");

        // Assert
        assertNull(result);
        verify(orderRepository).findById(999L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("deleteOrder should return success message when deleted")
    void deleteOrderShouldReturnSuccessMessageWhenDeleted() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        doNothing().when(orderRepository).delete(any(Order.class));

        // Act
        String result = orderService.deleteOrder(1L);

        // Assert
        assertEquals("Order successfully deleted", result);
        verify(orderRepository).findById(1L);
        verify(orderRepository).delete(any(Order.class));
    }

    @Test
    @DisplayName("deleteOrder should return not found message when not found")
    void deleteOrderShouldReturnNotFoundMessageWhenNotFound() {
        // Arrange
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        String result = orderService.deleteOrder(999L);

        // Assert
        assertEquals("Order not found", result);
        verify(orderRepository).findById(999L);
        verify(orderRepository, never()).delete(any(Order.class));
    }
}
