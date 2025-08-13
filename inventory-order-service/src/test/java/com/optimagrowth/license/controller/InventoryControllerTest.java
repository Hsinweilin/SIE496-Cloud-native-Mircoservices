package com.optimagrowth.license.controller;

import com.optimagrowth.license.model.Inventory;
import com.optimagrowth.license.service.InventoryService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
@org.springframework.test.context.ActiveProfiles("test")
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class InventoryControllerTest {

    @TestConfiguration
    @EnableWebSecurity
    @ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
    @org.springframework.core.annotation.Order(1)
    public static class TestSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            http.authorizeRequests()
                .anyRequest().permitAll();
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
    private InventoryService inventoryService;

    private Inventory inventory;
    private String inventoryJson;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        inventory.setInventoryId(1L);
        inventory.setProductName("Widget");
        inventory.setQuantity(100);
        inventory.setPrice(10);
        inventory.setDescription("A sample widget");

        inventoryJson = "{"
            + "\"inventoryId\": 1,"
            + "\"productName\": \"Widget\","
            + "\"quantity\": 100,"
            + "\"price\": 10,"
            + "\"description\": \"A sample widget\""
            + "}";
    }

    @Test
    @DisplayName("GET /v1/inventory/{inventoryId} - Found")
    void getInventoryByIdShouldReturnInventoryWhenFound() throws Exception {
        // Arrange
        when(inventoryService.getInventory(1L)).thenReturn(inventory);

        // Act & Assert
        mockMvc.perform(get("/v1/inventory/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.inventoryId").value(1))
            .andExpect(jsonPath("$.productName").value("Widget"))
            .andExpect(jsonPath("$.quantity").value(100))
            .andExpect(jsonPath("$.price").value(10))
            .andExpect(jsonPath("$.description").value("A sample widget"));
    }

    @Test
    @DisplayName("GET /v1/inventory/{inventoryId} - Not Found")
    void getInventoryByIdShouldReturn404WhenNotFound() throws Exception {
        // Arrange
        when(inventoryService.getInventory(999L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/v1/inventory/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /v1/inventory - Created")
    void createInventoryShouldCreateAndReturnInventory() throws Exception {
        // Arrange
        when(inventoryService.createInventory(any(Inventory.class))).thenReturn(inventory);

        // Act & Assert
        mockMvc.perform(post("/v1/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inventoryJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.inventoryId").value(1))
            .andExpect(jsonPath("$.productName").value("Widget"))
            .andExpect(jsonPath("$.quantity").value(100));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /v1/inventory/{inventoryId} - Updated")
    void updateInventoryShouldUpdateAndReturnInventory() throws Exception {
        // Arrange
        when(inventoryService.updateInventory(eq(1L), any(Inventory.class))).thenReturn(inventory);

        // Act & Assert
        mockMvc.perform(put("/v1/inventory/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inventoryJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.inventoryId").value(1))
            .andExpect(jsonPath("$.productName").value("Widget"))
            .andExpect(jsonPath("$.quantity").value(100));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /v1/inventory/{inventoryId} - Not Found")
    void updateInventoryShouldReturn404WhenNotFound() throws Exception {
        // Arrange
        when(inventoryService.updateInventory(eq(999L), any(Inventory.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/v1/inventory/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inventoryJson))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /v1/inventory/{inventoryId} - Deleted")
    void deleteInventoryShouldDeleteAndReturnSuccess() throws Exception {
        // Arrange
        when(inventoryService.deleteInventory(1L)).thenReturn("Inventory successfully deleted");

        // Act & Assert
        mockMvc.perform(delete("/v1/inventory/1"))
            .andExpect(status().isOk())
            .andExpect(content().string("Inventory successfully deleted"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /v1/inventory/{inventoryId} - Not Found")
    void deleteInventoryShouldReturn404WhenNotFound() throws Exception {
        // Arrange
        when(inventoryService.deleteInventory(999L)).thenReturn("Inventory not found");

        // Act & Assert
        mockMvc.perform(delete("/v1/inventory/999"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Inventory not found"));
    }
}
