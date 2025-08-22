# 7. Verify module circular dependency violations

Try to create a circular-dependency between two modules.

For example, try to autowire `InventoryService` in `OrderService`.

Run `ModularityTests` and the test should FAIL with the following error:

```shell
Cycle detected: Slice inventory -> 
                Slice orders -> 
                Slice inventory
```

[Next: 8. Explicit module dependencies](step-8.md)
