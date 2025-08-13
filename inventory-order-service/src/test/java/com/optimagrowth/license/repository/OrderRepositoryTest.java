package com.optimagrowth.license.repository;

import com.optimagrowth.license.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Order order1;
    private Order order2;

    /**
     * Set up test data before each test.
     */
    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();

        order1 = new Order();
        order1.setUserId(1L);
        order1.setProductName("Widget");
        order1.setQuantity(5);
        order1.setTotalPrice(500);
        order1.setOrderStatus("pending");
        orderRepository.save(order1);

        order2 = new Order();
        order2.setUserId(2L);
        order2.setProductName("Gadget");
        order2.setQuantity(3);
        order2.setTotalPrice(300);
        order2.setOrderStatus("completed");
        orderRepository.save(order2);
    }

    // --- Tests for findByUserId ---
    @Nested
    @DisplayName("findByUserId")
    class FindByUserIdTests {

        @Test
        @DisplayName("returns orders when user ID exists")
        void returnsOrdersWhenUserIdExists() {
            List<Order> found = orderRepository.findByUserId(1L);
            
            assertNotNull(found);
            assertEquals(1, found.size());
            assertEquals("Widget", found.get(0).getProductName());
            assertEquals(order1.getOrderId(), found.get(0).getOrderId());
        }

        @Test
        @DisplayName("returns empty list when user ID does not exist")
        void returnsEmptyListWhenUserIdDoesNotExist() {
            List<Order> found = orderRepository.findByUserId(999L);
            
            assertNotNull(found);
            assertTrue(found.isEmpty());
        }
    }

    // --- Tests for findByUserIdAndOrderId ---
    @Nested
    @DisplayName("findByUserIdAndOrderId")
    class FindByUserIdAndOrderIdTests {

        @Test
        @DisplayName("returns order when user ID and order ID match")
        void returnsOrderWhenUserIdAndOrderIdMatch() {
            Order found = orderRepository.findByUserIdAndOrderId(1L, order1.getOrderId());
            
            assertNotNull(found);
            assertEquals("Widget", found.getProductName());
            assertEquals(1L, found.getUserId());
            assertEquals(order1.getOrderId(), found.getOrderId());
        }

        @Test
        @DisplayName("returns null when user ID and order ID do not match")
        void returnsNullWhenUserIdAndOrderIdDoNotMatch() {
            Order found = orderRepository.findByUserIdAndOrderId(999L, order1.getOrderId());
            
            assertNull(found);
        }
    }
}
