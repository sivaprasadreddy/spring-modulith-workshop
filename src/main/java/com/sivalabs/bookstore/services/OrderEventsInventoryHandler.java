package com.sivalabs.bookstore.services;

import com.sivalabs.bookstore.models.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class OrderEventsInventoryHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderEventsInventoryHandler.class);
    private final InventoryService inventoryService;

    OrderEventsInventoryHandler(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @EventListener
    void handle(OrderCreatedEvent event) {
        log.info("[Inventory]: Received order created event: {}", event);
        inventoryService.decreaseStockLevel(event.productCode(), event.quantity());
    }
}
