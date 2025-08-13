
package com.optimagrowth.license;

import com.optimagrowth.license.model.Inventory;
import com.optimagrowth.license.repository.InventoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase
class InventoryRepositoryTest {

	@Autowired
	private InventoryRepository inventoryRepository;

	private Inventory inventory1;
	private Inventory inventory2;

	/**
	 * Set up test data before each test.
	 */
	@BeforeEach
	void setUp() {
		inventoryRepository.deleteAll();

		inventory1 = new Inventory();
		// Don't set ID manually, let the database generate it
		inventory1.setProductName("Widget");
		inventory1.setQuantity(100);
		inventory1.setPrice(100);
		inventoryRepository.save(inventory1);

		inventory2 = new Inventory();
		// Don't set ID manually, let the database generate it
		inventory2.setProductName("Gadget");
		inventory2.setQuantity(50);
		inventory2.setPrice(50);
		inventoryRepository.save(inventory2);
	}

	// --- Tests for findByProductName ---
	@Nested
	@DisplayName("findByProductName")
	class FindByProductNameTests {

		@Test
		@DisplayName("returns inventory when product name exists")
		void returnsInventoryWhenProductNameExists() {
			Inventory found = inventoryRepository.findByProductName("Widget");
			assertNotNull(found);
			assertEquals("Widget", found.getProductName());
			assertEquals(inventory1.getInventoryId(), found.getInventoryId());
		}

		@Test
		@DisplayName("returns null when product name does not exist")
		void returnsNullWhenProductNameDoesNotExist() {
			Inventory found = inventoryRepository.findByProductName("NonExistent");
			assertNull(found);
		}
	}

	// --- Tests for findByProductNameAndInventoryId ---
	@Nested
	@DisplayName("findByProductNameAndInventoryId")
	class FindByProductNameAndInventoryIdTests {

		@Test
		@DisplayName("returns inventory when product name and id match")
		void returnsInventoryWhenProductNameAndIdMatch() {
			Inventory found = inventoryRepository.findByProductNameAndInventoryId("Gadget", inventory2.getInventoryId());
			assertNotNull(found);
			assertEquals("Gadget", found.getProductName());
			assertEquals(inventory2.getInventoryId(), found.getInventoryId());
		}

		@Test
		@DisplayName("returns null when product name and id do not match")
		void returnsNullWhenProductNameAndIdDoNotMatch() {
			Inventory found = inventoryRepository.findByProductNameAndInventoryId("Widget", 999L);
			assertNull(found);
		}
	}
}