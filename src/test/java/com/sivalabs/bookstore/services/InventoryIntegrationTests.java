package com.sivalabs.bookstore.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.sivalabs.bookstore.TestcontainersConfiguration;
import com.sivalabs.bookstore.models.Customer;
import com.sivalabs.bookstore.models.OrderCreatedEvent;
import java.time.Duration;
import java.util.UUID;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class InventoryIntegrationTests {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private InventoryService inventoryService;

    @Test
    void handleOrderCreatedEvent() {
        var customer = new Customer("Siva", "siva@gmail.com", "9987654");
        var productCode = "P114";
        var event = new OrderCreatedEvent(UUID.randomUUID().toString(), productCode, 2, customer);
        eventPublisher.publishEvent(event);

        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            assertThat(inventoryService.getStockLevel(productCode)).isEqualTo(598);
        });
    }
}
