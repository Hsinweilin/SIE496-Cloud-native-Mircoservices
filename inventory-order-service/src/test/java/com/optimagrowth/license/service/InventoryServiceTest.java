package com.optimagrowth.license.service;

import com.optimagrowth.license.model.Inventory;
import com.optimagrowth.license.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        inventory.setInventoryId(1L);
        inventory.setProductName("Widget");
        inventory.setQuantity(100);
        inventory.setPrice(10);
        inventory.setDescription("A sample widget");
    }

    @Test
    @DisplayName("createInventory should save and return inventory")
    void createInventoryShouldSaveAndReturnInventory() {
        // Arrange
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        // Act
        Inventory result = inventoryService.createInventory(inventory);

        // Assert
        assertNotNull(result);
        assertEquals(inventory.getProductName(), result.getProductName());
        assertEquals(inventory.getQuantity(), result.getQuantity());
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    @DisplayName("getInventory should return inventory when found")
    void getInventoryShouldReturnInventoryWhenFound() {
        // Arrange
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));

        // Act
        Inventory result = inventoryService.getInventory(1L);

        // Assert
        assertNotNull(result);
        assertEquals(inventory.getProductName(), result.getProductName());
        verify(inventoryRepository).findById(1L);
    }

    @Test
    @DisplayName("getInventory should return null when not found")
    void getInventoryShouldReturnNullWhenNotFound() {
        // Arrange
        when(inventoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Inventory result = inventoryService.getInventory(999L);

        // Assert
        assertNull(result);
        verify(inventoryRepository).findById(999L);
    }

    @Test
    @DisplayName("updateInventory should update and return inventory when found")
    void updateInventoryShouldUpdateAndReturnInventoryWhenFound() {
        // Arrange
        Inventory updatedInventory = new Inventory();
        updatedInventory.setProductName("Updated Widget");
        updatedInventory.setQuantity(200);
        updatedInventory.setPrice(20);
        updatedInventory.setDescription("An updated widget");

        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Inventory result = inventoryService.updateInventory(1L, updatedInventory);

        // Assert
        assertNotNull(result);
        assertEquals(updatedInventory.getProductName(), result.getProductName());
        assertEquals(updatedInventory.getQuantity(), result.getQuantity());
        assertEquals(updatedInventory.getPrice(), result.getPrice());
        assertEquals(updatedInventory.getDescription(), result.getDescription());
        verify(inventoryRepository).findById(1L);
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    @DisplayName("updateInventory should return null when not found")
    void updateInventoryShouldReturnNullWhenNotFound() {
        // Arrange
        when(inventoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Inventory result = inventoryService.updateInventory(999L, inventory);

        // Assert
        assertNull(result);
        verify(inventoryRepository).findById(999L);
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }

    @Test
    @DisplayName("deleteInventory should return success message when deleted")
    void deleteInventoryShouldReturnSuccessMessageWhenDeleted() {
        // Arrange
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
        doNothing().when(inventoryRepository).delete(any(Inventory.class));

        // Act
        String result = inventoryService.deleteInventory(1L);

        // Assert
        assertEquals("Inventory successfully deleted", result);
        verify(inventoryRepository).findById(1L);
        verify(inventoryRepository).delete(any(Inventory.class));
    }

    @Test
    @DisplayName("deleteInventory should return not found message when not found")
    void deleteInventoryShouldReturnNotFoundMessageWhenNotFound() {
        // Arrange
        when(inventoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        String result = inventoryService.deleteInventory(999L);

        // Assert
        assertEquals("Inventory not found", result);
        verify(inventoryRepository).findById(999L);
        verify(inventoryRepository, never()).delete(any(Inventory.class));
    }
}
