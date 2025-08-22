# 6. Verify module boundary violations

Currently, `OrderService` is trying to access `catalog` module's internal component `ProductService`.

Run `ModularityTests` and the test should FAIL with the following error:

```shell
- Module 'orders' depends on non-exposed type com.sivalabs.bookstore.catalog.domain.ProductService within module 'catalog'!
```

**Create CatalogApi.java and add the following code:**

```java
package com.sivalabs.bookstore.catalog;

import org.springframework.stereotype.Service;

@Service
public class CatalogApi {
    private final ProductService productService;
    public CatalogApi(ProductService productService) {
        this.productService = productService;
    }

    public Optional<Product> getByCode(String code) {
        return productService.getByCode(code);
    }
}
```

Now, in `OrderService` we can access `CatalogApi` and use it to get the product details.

[Next: 7. Verify module circular dependency violations](step-7.md)