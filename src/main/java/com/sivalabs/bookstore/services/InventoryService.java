package com.sivalabs.bookstore.services;

import com.sivalabs.bookstore.entities.InventoryEntity;
import com.sivalabs.bookstore.repositories.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class InventoryService {
    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository inventoryRepository;

    InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void decreaseStockLevel(String productCode, int quantity) {
        log.info("Decrease stock level for product code {} and quantity {}", productCode, quantity);
        var inventory = inventoryRepository.findByProductCode(productCode).orElse(null);
        if (inventory != null) {
            long newQuantity = inventory.getQuantity() - quantity;
            inventory.setQuantity(newQuantity);
            inventoryRepository.save(inventory);
            log.info("Updated stock level for product code {} to : {}", productCode, newQuantity);
        } else {
            log.warn("Invalid product code {}", productCode);
        }
    }

    public Long getStockLevel(String productCode) {
        Long stock = inventoryRepository
                .findByProductCode(productCode)
                .map(InventoryEntity::getQuantity)
                .orElse(0L);
        log.info("Stock level for product code {} is : {}", productCode, stock);
        return stock;
    }
}
