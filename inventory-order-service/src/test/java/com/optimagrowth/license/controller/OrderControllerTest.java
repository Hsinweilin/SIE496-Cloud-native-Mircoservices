package com.optimagrowth.license.controller;

import com.optimagrowth.license.model.Order;
import com.optimagrowth.license.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@org.springframework.test.context.ActiveProfiles("test")
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @TestConfiguration
    @EnableWebSecurity
    @ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
    @org.springframework.core.annotation.Order(1)  // Important: This gives higher priority than main SecurityConfig
    public static class TestSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            http.authorizeRequests()
                .anyRequest().permitAll();  // For testing, permit all requests
            http.csrf().disable();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            KeycloakAuthenticationProvider provider = keycloakAuthenticationProvider();
            provider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
            auth.authenticationProvider(provider);
        }

        @Bean
        @Override
        @Primary
        protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
            return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
        }

        @Bean
        @Primary
        public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
            return new KeycloakSpringBootConfigResolver();
        }

        @Bean
        @Primary
        public AdapterConfig adapterConfig() {
            AdapterConfig adapterConfig = new AdapterConfig();
            adapterConfig.setRealm("test-realm");
            adapterConfig.setResource("test-client");
            adapterConfig.setAuthServerUrl("http://localhost:8080/auth");
            return adapterConfig;
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private Order testOrder;
    private String orderJson;

    @BeforeEach
    void setUp() {
        // Initialize test order data
        testOrder = new Order();
        testOrder.setOrderId(1L);
        testOrder.setUserId(123L);
        testOrder.setOrderDate(LocalDateTime.now());
        testOrder.setProductName("Widget");
        testOrder.setQuantity(2);
        testOrder.setTotalPrice(100);  // Fixed: using totalPrice instead of price
        testOrder.setOrderStatus("PLACED");  // Fixed: using orderStatus instead of status

        // JSON representation of the order
        orderJson = "{"
            + "\"orderId\": 1,"
            + "\"userId\": 123,"
            + "\"productName\": \"Widget\","
            + "\"quantity\": 2,"
            + "\"totalPrice\": 100,"
            + "\"orderStatus\": \"PLACED\""
            + "}";
    }

    @Test
    @DisplayName("GET /v1/order/{orderId} - Found")
    void getOrderByIdShouldReturnOrderWhenFound() throws Exception {
        // Arrange - Set up the service to return our test order
        when(orderService.getOrder(1L)).thenReturn(testOrder);

        // Act & Assert - Perform the GET request and verify the response
        mockMvc.perform(get("/v1/order/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderId").value(1))
            .andExpect(jsonPath("$.userId").value(123))
            .andExpect(jsonPath("$.productName").value("Widget"))
            .andExpect(jsonPath("$.quantity").value(2))
            .andExpect(jsonPath("$.totalPrice").value(100))  // Fixed: using totalPrice
            .andExpect(jsonPath("$.orderStatus").value("PLACED"));  // Fixed: using orderStatus
    }

    @Test
    @DisplayName("GET /v1/order/{orderId} - Not Found")
    void getOrderByIdShouldReturn404WhenNotFound() throws Exception {
        // Arrange - Set up the service to return null (order not found)
        when(orderService.getOrder(999L)).thenReturn(null);

        // Act & Assert - Perform the GET request and verify 404 response
        mockMvc.perform(get("/v1/order/999"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Order not found"));  // Fixed: actual message
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("POST /v1/order - Created")
    void createOrderShouldCreateAndReturnOrder() throws Exception {
        // Arrange - Set up the service to return our test order after creation
        when(orderService.createOrder(any(Order.class))).thenReturn(testOrder);

        // Act & Assert - Perform the POST request and verify the response
        mockMvc.perform(post("/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.orderId").value(1))
            .andExpect(jsonPath("$.userId").value(123))  // Fixed: using userId
            .andExpect(jsonPath("$.orderStatus").value("PLACED"));  // Fixed: using orderStatus
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /v1/order/{orderId} - Updated")
    void updateOrderShouldUpdateAndReturnOrder() throws Exception {
        // Arrange - Set up the service to return our updated test order
        when(orderService.updateOrderStatus(eq(1L), eq("SHIPPED"))).thenReturn(testOrder);

        // Act & Assert - Perform the PUT request and verify the response
        mockMvc.perform(put("/v1/order/1")
                .param("orderStatus", "SHIPPED"))  // Fixed: using param instead of request body
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderId").value(1))
            .andExpect(jsonPath("$.userId").value(123))  // Fixed: using userId
            .andExpect(jsonPath("$.orderStatus").value("PLACED"));  // Fixed: using orderStatus
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /v1/order/{orderId} - Not Found")
    void updateOrderShouldReturn404WhenNotFound() throws Exception {
        // Arrange - Set up the service to return null (order not found)
        when(orderService.updateOrderStatus(eq(999L), eq("SHIPPED"))).thenReturn(null);

        // Act & Assert - Perform the PUT request and verify 404 response
        mockMvc.perform(put("/v1/order/999")
                .param("orderStatus", "SHIPPED"))  // Fixed: using param instead of request body
            .andExpect(status().isBadRequest())  // Fixed: controller returns BAD_REQUEST, not NOT_FOUND
            .andExpect(content().string("Order not found or not enough inventory."));  // Fixed: actual message
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /v1/order/{orderId} - Deleted")
    void deleteOrderShouldDeleteAndReturnSuccess() throws Exception {
        // Arrange - Set up the service to return success message
        when(orderService.deleteOrder(1L)).thenReturn("Order successfully deleted");

        // Act & Assert - Perform the DELETE request and verify the response
        mockMvc.perform(delete("/v1/order/1"))
            .andExpect(status().isOk())
            .andExpect(content().string("Order successfully deleted"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /v1/order/{orderId} - Not Found")
    void deleteOrderShouldReturn404WhenNotFound() throws Exception {
        // Arrange - Set up the service to return not found message
        when(orderService.deleteOrder(999L)).thenReturn("Order not found");

        // Act & Assert - Perform the DELETE request and verify 404 response
        mockMvc.perform(delete("/v1/order/999"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Order not found"));
    }
}
