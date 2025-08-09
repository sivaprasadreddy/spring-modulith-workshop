# 6. Verify module boundary violations

**6. Try to access another module's internal component.**

Autowire `ProductRepository` in `OrderServiceImpl`.

Run `ModularityTests` and the test should FAIL with the following error:

```shell
- Module 'orders' depends on non-exposed type com.sivalabs.bookstore.catalog.domain.ProductRepository within module 'catalog'!
```


[Next: 7. Verify module circular dependency violations](step-7.md)