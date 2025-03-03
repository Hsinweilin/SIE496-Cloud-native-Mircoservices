package com.optimagrowth.license.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.optimagrowth.license.model.Inventory;

@Repository
public interface InventoryRepository extends CrudRepository<Inventory, Long> {
    public Inventory findByProductName(String productName);
    public Inventory findByProductNameAndInventoryId(String productName, Long inventoryId);
}
