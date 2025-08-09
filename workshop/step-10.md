# 10. Testing modules in isolation



**9. Testing modules independently using `@ApplicationModuleTest`**

Replace `@SpringBootTest` with `@ApplicationModuleTest`
in `ProductRestControllerTests`, `InventoryIntegrationTests`, `OrderRestControllerTests`.

**10. Verify event published or not.**

In `OrderRestControllerTests`, update `shouldCreateOrderSuccessfully()` test as follows:

```java
@Test
void shouldCreateOrderSuccessfully(AssertablePublishedEvents events) throws Exception {
    mockMvc.perform(
        post("/api/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(
            """
             ...
             ...
           """))
        .andExpect(status().isCreated());

    assertThat(events)
            .contains(OrderCreatedEvent.class)
            .matching(e -> e.customer().email(), "siva123@gmail.com")
            .matching(OrderCreatedEvent::productCode, "P100");
}
```

Check the `event_publication` table for the event processing history.

Explain external event publication support.

**11. Publish event and verify the expected behavior.**

In `InventoryIntegrationTests`, update `handleOrderCreatedEvent()` test as follows:

```java
@Test
void handleOrderCreatedEvent(Scenario scenario) {
    var customer = new Customer("Siva", "siva@gmail.com", "9987654");
    String productCode = "P114";
    var event = new OrderCreatedEvent(UUID.randomUUID().toString(), productCode, 2, customer);
    scenario.publish(event).andWaitForStateChange(() -> inventoryService.getStockLevel(productCode) == 598);
}
```

[Next: 11. Create C4 Model documentation](step-11.md)
