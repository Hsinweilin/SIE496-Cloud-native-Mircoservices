package com.optimagrowth.license.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.Inventory;
import com.optimagrowth.license.repository.InventoryRepository;
import org.springframework.http.HttpStatus;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    // Create an inventory item
	public Inventory createInventory(Inventory inventory) {
		return inventoryRepository.save(inventory);
	}

	// Update an existing inventory item
    public Inventory updateInventory(Long inventoryId, Inventory inventory) {
        // Fetch the existing inventory item by ID
        Inventory existingInventory = inventoryRepository.findById(inventoryId).orElse(null);
        
        // If inventory is not found, return null
        if (existingInventory == null) {
            return null; // Return null if inventory not found
        }

        // Update the fields of the existing inventory item
        existingInventory.setProductName(inventory.getProductName());
        existingInventory.setQuantity(inventory.getQuantity());
        existingInventory.setPrice(inventory.getPrice());
        existingInventory.setDescription(inventory.getDescription());

        // Save the updated inventory item to the repository
        return inventoryRepository.save(existingInventory); // Return the updated inventory
    }

    // Get inventory by ID
    public Inventory getInventory(Long inventoryId) {
        return inventoryRepository.findById(inventoryId).orElse(null);
    }

    // // Get inventory by product name
    // public List<Inventory> getInventoryByProductName(String productName) {
    //     return inventoryRepository.findByProductName(productName);
    // }

    public HttpStatus deleteInventory(Long inventoryId){
        try {
            Inventory inventory = inventoryRepository.findById(inventoryId).orElse(null);
            if (inventory == null) {
                return HttpStatus.NOT_FOUND; // User not found
            }
            // Delete the user if it exists
            inventoryRepository.delete(inventory);
            return HttpStatus.OK; // User successfully deleted
        } catch (Exception e) {
            // Handle any unexpected errors
            return HttpStatus.INTERNAL_SERVER_ERROR; // Internal server error
        }
    }
}

