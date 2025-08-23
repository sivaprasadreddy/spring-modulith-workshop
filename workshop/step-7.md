# 7. Verify module circular dependency violations

Spring Modulith checks for circular dependencies between modules and reports it as a violation.

The `orders` module depends on the `catalog` module.

Let's try to create a circular-dependency between two modules.

For example, try to use `OrderCreatedEvent` in `CatalogApi`.

```java
public Optional<Product> getByCode(String code) {
    OrderCreatedEvent event = new OrderCreatedEvent(null, null, 0, null);
    return productService.getByCode(code);
}
```

Run `ModularityTest` and the test should FAIL with the following error:

```shell
Cycle detected: Slice catalog -> 
                Slice orders -> 
                Slice catalog
```

[Next: 8. Explicit module dependencies](step-8.md)
