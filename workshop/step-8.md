# 8. Explicit module dependencies

By default, a module can depend on any other module if there are no circular dependencies.
But we can explicitly specify module dependencies to restrict to a specific set of modules.

Add `package-info.java` in `com.sivalabs.bookstore.orders` package with the following content:

```java
@ApplicationModule(allowedDependencies = {"catalog"})
package com.sivalabs.bookstore.orders;

import org.springframework.modulith.ApplicationModule;
```

[Next: 9. Event Driven Communication](step-9.md)