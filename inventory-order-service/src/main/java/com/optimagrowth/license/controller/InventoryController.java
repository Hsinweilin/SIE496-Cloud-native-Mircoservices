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
import org.springframework.http.HttpStatus;

import com.optimagrowth.license.model.Inventory;
import com.optimagrowth.license.service.InventoryService;

@RestController
@RequestMapping(value="v1/inventory")
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;

    // Get inventory by ID
    @GetMapping("/{inventoryId}")
    public ResponseEntity<Inventory> getInventory(@PathVariable("inventoryId") Long inventoryId) {
        Inventory inventory = inventoryService.getInventory(inventoryId);

        if (inventory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Return 404 if not found
        }

        return ResponseEntity.status(HttpStatus.OK).body(inventory);  // Return 200 OK with inventory
    }

    // Update inventory by ID
    @PutMapping("/{inventoryId}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long inventoryId, @RequestBody Inventory inventory) {
        Inventory updatedInventory = inventoryService.updateInventory(inventoryId, inventory);

        if (updatedInventory == null) {        
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Return 404 if inventory not found
        }

        return ResponseEntity.status(HttpStatus.OK).body(updatedInventory);  // Return 200 OK with updated inventory
    }

    // Create a new inventory item
    @PostMapping
    public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) {
        Inventory createdInventory = inventoryService.createInventory(inventory);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(createdInventory);  // Return 201 Created with new inventory
    }

    // Delete inventory by ID
    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<String> deleteInventory(@PathVariable("inventoryId") Long inventoryId) {
        String status = inventoryService.deleteInventory(inventoryId);

        // Return appropriate response based on status
        if ("Inventory not found".equals(status)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(status);  // Return 404 if inventory not found
        } else if ("Inventory successfully deleted".equals(status)) {
            return ResponseEntity.status(HttpStatus.OK)
                                 .body(status);  // Return 200 OK if inventory deleted
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred while deleting the inventory item");  // Return 500 Internal Server Error
        }
    }
}
