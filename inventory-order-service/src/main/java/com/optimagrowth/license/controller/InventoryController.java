package com.optimagrowth.license.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    @RequestMapping(value="/{inventoryId}", method = RequestMethod.GET)
    public ResponseEntity<Inventory> getInventory(@PathVariable("inventoryId") Long inventoryId) {
    	Inventory inventory = inventoryService.getInventory(inventoryId);
    	if (inventory == null) {
    		return ResponseEntity.notFound().build(); // Return 404 if not found
    	}
    	return ResponseEntity.ok(inventory); // Return 200 OK with inventory
    }
    
    // Update inventory by ID
    @PutMapping("/{inventoryId}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long inventoryId, @RequestBody Inventory inventory) {
    	Inventory updatedInventory = inventoryService.updateInventory(inventoryId, inventory);
    	if (updatedInventory == null) {
    		return ResponseEntity.notFound().build(); // Return 404 if inventory not found
    	}
    	return ResponseEntity.ok(updatedInventory); // Return 200 OK with updated inventory
    }
    
    // Create a new inventory item
    @PostMapping
    public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) {
    	Inventory createdInventory = inventoryService.createInventory(inventory);
    	return new ResponseEntity<>(createdInventory, HttpStatus.CREATED); // Return 201 Created with new inventory
    }
    
    // Delete inventory by ID
    @DeleteMapping(value="/{inventoryId}")
    public ResponseEntity<String> deleteInventory(@PathVariable("inventoryId") Long inventoryId) {
    	HttpStatus status = inventoryService.deleteInventory(inventoryId); // Call service to delete
    	// Return appropriate response based on status
    	if (status == HttpStatus.NOT_FOUND) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND)
    							 .body("Inventory item not found");
    	} else if (status == HttpStatus.OK) {
    		return ResponseEntity.status(HttpStatus.OK)
    							 .body("Inventory item deleted successfully");
    	} else {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    							 .body("An error occurred while deleting the inventory item");
    	}
    }
}
