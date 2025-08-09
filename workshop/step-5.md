# 5. Understand NamedInterfaces


**5. Expose `com.sivalabs.bookstore.orders.domain.models` and `com.sivalabs.bookstore.orders.domain.events` packages as named-interfaces.**

Add `package-info.java` in `com.sivalabs.bookstore.orders.domain.models` package with the following content:

```java
@NamedInterface("order-models")
package com.sivalabs.bookstore.orders.domain.models;

import org.springframework.modulith.NamedInterface;
```

Add `package-info.java` in `com.sivalabs.bookstore.orders.domain.events` package with the following content:

```java
@NamedInterface("order-events")
package com.sivalabs.bookstore.orders.domain.events;

import org.springframework.modulith.NamedInterface;
```

Run `ModularityTests` and the test should PASS.

[Next: 6. Verify module boundary violations](step-6.md)