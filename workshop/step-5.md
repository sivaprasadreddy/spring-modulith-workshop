# 5. Understand NamedInterfaces

The Module 'inventory' (`OrderEventsInventoryHandler`) depends on non-exposed type 'com.sivalabs.bookstore.orders.domain.models.OrderCreatedEvent' from module 'orders'.

We can expose additional packages or types to other modules using `NamedInterface` annotation.

How to fix?

* Option#1: Add `NamedInterface` annotation to `OrderCreatedEvent` class.
* Option#2: Move `OrderCreatedEvent` into `com.sivalabs.bookstore.orders.domain.models` package and add `NamedInterface` annotation to `package-info.java`.


Expose `com.sivalabs.bookstore.orders.domain.models` package as named-interface.

Add `package-info.java` in `com.sivalabs.bookstore.orders.domain.models` package with the following content:

```java
@NamedInterface("order-models")
package com.sivalabs.bookstore.orders.domain.models;

import org.springframework.modulith.NamedInterface;
```

Run `ModularityTest`.

[Next: 6. Verify module boundary violations](step-6.md)