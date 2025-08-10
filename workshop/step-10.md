# 10. Testing modules in isolation

* We can test modules independently using `@ApplicationModuleTest`.
* BootstrapMode: `STANDALONE`(default), `DIRECT_DEPENDENCIES`, `ALL_DEPENDENCIES`.

## Testing modules by loading the beans of that module

* Replace `@SpringBootTest` with `@ApplicationModuleTest`
in `ProductRestControllerTests`, `InventoryIntegrationTests`, `OrderRestControllerTests`.
* Provide dependencies of other modules as MockBeans

In `OrderRestControllerTests`, update `shouldCreateOrderSuccessfully()` test as follows:

```java
@ApplicationModuleTest(webEnvironment = RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@AutoConfigureMockMvc
class OrderRestControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @MockitoBean
    ProductApi productApi;

    @BeforeEach
    void setUp() {
        ProductDto product = new ProductDto("P100", "The Hunger Games", "", null, new BigDecimal("34.0"));
        given(productApi.getByCode("P100")).willReturn(Optional.of(product));
    }
    
    //....
}

```

## Verify event published successfully
If the module publishes an event, we can use `AssertablePublishedEvents` to verify the event published successfully.

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


## Testing inbound event handlers

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
