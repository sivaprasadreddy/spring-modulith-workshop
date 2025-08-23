# 5. Understand NamedInterfaces

The `inventory` module (`OrderEventsInventoryHandler`) depends on non-exposed type `com.sivalabs.bookstore.orders.domain.models.OrderCreatedEvent` from `orders` module.

We can expose additional types or packages to other modules using `@NamedInterface` annotation.

**How to fix?**

* **Option#1:** Add `NamedInterface` annotation to `OrderCreatedEvent` class.
* **Option#2:** Move `OrderCreatedEvent` into `com.sivalabs.bookstore.orders.domain.models` package and add `NamedInterface` annotation to `package-info.java`.

Expose `com.sivalabs.bookstore.orders.domain.models` package as named-interface.

Add `package-info.java` in `com.sivalabs.bookstore.orders.domain.models` package with the following content:

```java
@NamedInterface("order-models")
package com.sivalabs.bookstore.orders.domain.models;

import org.springframework.modulith.NamedInterface;
```

Run `ModularityTest`.

[Previous: 4. Understand OPEN type modules](step-4.md)
[Next: 6. Verify module boundary violations](step-6.md)