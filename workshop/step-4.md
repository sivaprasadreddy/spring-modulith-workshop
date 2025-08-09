# 4. Understand OPEN type modules

1. **Move `PagedResult` class into `common/models` package.**`

```
- Module 'catalog' depends on non-exposed type com.sivalabs.bookstore.common.models.PagedResult within module 'common'!

.....
.....
```

2. **Make `common` module `OPEN` type module.**

Add `package-info.java` in `com.sivalabs.bookstore.common` package with the following content:

```java
@ApplicationModule(type = ApplicationModule.Type.OPEN)
package com.sivalabs.bookstore.common;

import org.springframework.modulith.ApplicationModule;
```

Run `ModularityTests`.

[Next: 5. Understand NamedInterfaces](step-5.md)