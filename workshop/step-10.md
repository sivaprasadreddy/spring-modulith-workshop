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
    CatalogApi catalogApi;

    @BeforeEach
    void setUp() {
        Product product = new Product("P100", "The Hunger Games", "", null, new BigDecimal("34.0"));
        given(catalogApi.getByCode("P100")).willReturn(Optional.of(product));
    }
    
    //....
}
```

Check in the logs that only the specified module dependencies are loaded.

## Verify event published successfully
If the module publishes an event, we can use `AssertablePublishedEvents` to verify the event published successfully.

```java
@Test
void shouldCreateOrderSuccessfully(AssertablePublishedEvents events) throws Exception {
    MvcTestResult testResult = mockMvcTester.post().uri("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {
                       ...
                    }
                    """)
            .exchange();
    assertThat(testResult).hasStatus(HttpStatus.CREATED);

    assertThat(events)
            .contains(OrderCreatedEvent.class)
            .matching(e -> e.customer().email(), "siva123@gmail.com")
            .matching(OrderCreatedEvent::productCode, "P100");
}
```

## Testing inbound event handlers

In `InventoryIntegrationTests`, update `handleOrderCreatedEvent()` test as follows:

```java
@ApplicationModuleTest(webEnvironment = RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class InventoryIntegrationTests {
    //...
    @Test
    void handleOrderCreatedEvent(Scenario scenario) {
        var productCode = "P114";
        var customer = new Customer("Siva", "siva@gmail.com", "9987654");
        var event = new OrderCreatedEvent(UUID.randomUUID().toString(), productCode, 2, customer);

        scenario.publish(event)
                .andWaitForStateChange(() -> inventoryService.getStockLevel(productCode) == 598)
                .andVerify(result -> assertThat(result).isTrue());
    }
}
```

[Previous: 9. Event Driven Communication](step-9.md)
[Next: 11. Create C4 Model documentation](step-11.md)
