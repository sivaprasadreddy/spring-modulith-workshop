# 7. Verify module circular dependency violations

**8. Try to create circular-dependency between two modules.**

Make `InventoryService` as a `public` class and autowire in `OrderServiceImpl`.

Run `ModularityTests` and the test should FAIL with the following error:

```shell
Cycle detected: Slice inventory -> 
                Slice orders -> 
                Slice inventory
```

[Next: 8. Explicit module dependencies](step-8.md)