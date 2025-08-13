package com.optimagrowth.payment.controller;

import com.optimagrowth.payment.model.Payment;
import com.optimagrowth.payment.service.PaymentService;
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

@WebMvcTest(PaymentController.class)
@org.springframework.test.context.ActiveProfiles("test")
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class PaymentControllerTest {
    @TestConfiguration
    @EnableWebSecurity
    @ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
    @org.springframework.core.annotation.Order(1)
    public static class TestSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            http.authorizeRequests().anyRequest().permitAll();
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
    private PaymentService paymentService;
    private Payment testPayment;
    private String paymentJson;
    @BeforeEach
    void setUp() {
        testPayment = new Payment();
        testPayment.setPaymentId(1L);
        testPayment.setUserId(123L);
        testPayment.setOrderId(456L);
        testPayment.setAmount(100);
        testPayment.setPaymentStatus("pending");
        testPayment.setPaymentDate(LocalDateTime.now());
        paymentJson = "{"
            + "\"paymentId\": 1,"
            + "\"userId\": 123,"
            + "\"orderId\": 456,"
            + "\"amount\": 100,"
            + "\"paymentStatus\": \"pending\""
            + "}";
    }
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("POST /v1/payment - Created")
    void createPaymentShouldCreateAndReturnPayment() throws Exception {
        when(paymentService.createPayment(any(Payment.class))).thenReturn(testPayment);
        mockMvc.perform(post("/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paymentJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.paymentId").value(1))
            .andExpect(jsonPath("$.userId").value(123))
            .andExpect(jsonPath("$.orderId").value(456))
            .andExpect(jsonPath("$.amount").value(100))
            .andExpect(jsonPath("$.paymentStatus").value("pending"));
    }
    @Test
    @DisplayName("GET /v1/payment/{paymentId} - Found")
    void getPaymentByIdShouldReturnPaymentWhenFound() throws Exception {
        when(paymentService.getPayment(1L)).thenReturn(testPayment);
        mockMvc.perform(get("/v1/payment/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.paymentId").value(1))
            .andExpect(jsonPath("$.userId").value(123))
            .andExpect(jsonPath("$.orderId").value(456))
            .andExpect(jsonPath("$.amount").value(100))
            .andExpect(jsonPath("$.paymentStatus").value("pending"));
    }
    @Test
    @DisplayName("GET /v1/payment/{paymentId} - Not Found")
    void getPaymentByIdShouldReturn404WhenNotFound() throws Exception {
        when(paymentService.getPayment(999L)).thenReturn(null);
        mockMvc.perform(get("/v1/payment/999"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Payment not found"));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /v1/payment/{paymentId} - Updated")
    void updatePaymentShouldUpdateAndReturnPayment() throws Exception {
        when(paymentService.updatePaymentStatus(eq(1L), eq("completed"))).thenReturn(testPayment);
        mockMvc.perform(put("/v1/payment/1")
                .param("paymentStatus", "completed"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.paymentId").value(1))
            .andExpect(jsonPath("$.paymentStatus").value("pending"));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /v1/payment/{paymentId} - Not Found")
    void updatePaymentShouldReturn404WhenNotFound() throws Exception {
        when(paymentService.updatePaymentStatus(eq(999L), eq("completed"))).thenReturn(null);
        mockMvc.perform(put("/v1/payment/999")
                .param("paymentStatus", "completed"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Payment not found or invalid payment status."));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /v1/payment/{paymentId} - Deleted")
    void deletePaymentShouldDeleteAndReturnSuccess() throws Exception {
        when(paymentService.deletePayment(1L)).thenReturn("Payment successfully deleted");
        mockMvc.perform(delete("/v1/payment/1"))
            .andExpect(status().isOk())
            .andExpect(content().string("Payment successfully deleted"));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /v1/payment/{paymentId} - Not Found")
    void deletePaymentShouldReturn404WhenNotFound() throws Exception {
        when(paymentService.deletePayment(999L)).thenReturn("Payment not found");
        mockMvc.perform(delete("/v1/payment/999"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Payment not found"));
    }
}
