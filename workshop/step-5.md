# 5. Understand NamedInterfaces

We can expose additional packages or types to other modules using `NamedInterface` annotation.

Expose `com.sivalabs.bookstore.orders.domain.models` package as named-interface.

Add `package-info.java` in `com.sivalabs.bookstore.orders.domain.models` package with the following content:

```java
@NamedInterface("order-models")
package com.sivalabs.bookstore.orders.domain.models;

import org.springframework.modulith.NamedInterface;
```

Run `ModularityTests`.

[Next: 6. Verify module boundary violations](step-6.md)