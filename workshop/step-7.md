# 7. Verify module circular dependency violations

Try to create a circular-dependency between two modules.

Make `InventoryService` as a `public` class and autowire in `OrderService`.

Run `ModularityTests` and the test should FAIL with the following error:

```shell
Cycle detected: Slice inventory -> 
                Slice orders -> 
                Slice inventory
```

[Next: 8. Explicit module dependencies](step-8.md)
