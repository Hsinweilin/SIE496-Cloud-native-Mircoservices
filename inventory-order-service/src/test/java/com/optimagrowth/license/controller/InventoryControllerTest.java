package com.optimagrowth.license.controller;

import com.optimagrowth.license.model.Inventory;
import com.optimagrowth.license.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.Mockito;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @MockBean
    private AdapterConfig adapterConfig;

    private Inventory inventory;
    private String inventoryJson;

    @BeforeEach
    void setUp() {
        Mockito.when(adapterConfig.getRealm()).thenReturn("test-realm");
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
