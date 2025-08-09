# 8. Explicit module dependencies

**7. Explicitly specify/restrict module dependencies.**

Add `package-info.java` in `com.sivalabs.bookstore.orders` package with the following content:

```java
@ApplicationModule(allowedDependencies = {"catalog"})
package com.sivalabs.bookstore.orders;

import org.springframework.modulith.ApplicationModule;
```

[Next: 9. Event Driven Communication](step-9.md)