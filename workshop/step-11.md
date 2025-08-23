# 11. Create C4 Model documentation


In `ModularityTest.java` update the `verifiesModularStructure()` test.

```java
package com.sivalabs.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModularityTest {
    static ApplicationModules modules = ApplicationModules.of(BookStoreApplication.class);

    @Test
    void verifiesModularStructure() {
        modules.verify();
        new Documenter(modules).writeDocumentation();
    }
}
```

The C4 Model documentation is generated under `target/spring-modulith-docs` directory.


[Previous: 10. Testing modules in isolation](step-10.md) &nbsp;&nbsp;&nbsp;&nbsp;
[Next: 12. Conclusion](step-12.md)

