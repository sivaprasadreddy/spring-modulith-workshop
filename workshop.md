# Spring Modulith Workshop

## Prerequisites
* JDK 21
* Docker and Docker Compose
* Your favourite IDE (Recommended: [IntelliJ IDEA](https://www.jetbrains.com/idea/))

Pulling docker images may take some time, so it's better to pull them before workshop begin.

```shell
$ docker pull postgres:17-alpine
$ docker pull rabbitmq:4.0.6-management
$ docker pull rabbitmq:4.0.6-alpine
$ docker pull openzipkin/zipkin:3.4.4
```

## Project Local Setup

```shell
$ git clone https://github.com/sivaprasadreddy/spring-monolith-workshop.git
$ cd spring-monolith-workshop
$ ./mvnw clean verify
```

## Exercises

**1. Refactor code to follow package-by-feature**

The existing code can be refactored to have the following structure:

```shell
- common
  - models
- catalog
  - domain
  - web
- orders
  - domain
    - models
    - events
  - web
- inventory
- notifications
```

Create the directory structure using the following command:

```shell
mkdir -p {common/models,catalog/{domain,web},orders/{domain/{models,events},web},inventory,notifications}
```

**2. Make sure the following `spring-modulith` dependencies are added to `pom.xml`.**

For convenience, these dependencies are already added.

```xml
<properties>
    <spring-modulith.version>1.3.2</spring-modulith.version>
</properties>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.modulith</groupId>
            <artifactId>spring-modulith-bom</artifactId>
            <version>${spring-modulith.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <!-- other dependencies -->
    <dependency>
        <groupId>org.springframework.modulith</groupId>
        <artifactId>spring-modulith-starter-core</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.modulith</groupId>
        <artifactId>spring-modulith-starter-jdbc</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.modulith</groupId>
        <artifactId>spring-modulith-events-amqp</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.modulith</groupId>
        <artifactId>spring-modulith-actuator</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.modulith</groupId>
        <artifactId>spring-modulith-observability</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.modulith</groupId>
        <artifactId>spring-modulith-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

**3. Create a test that verifies modularity.**

```java
package com.sivalabs.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModularityTests {
    static ApplicationModules modules = ApplicationModules.of(BookStoreApplication.class);

    @Test
    void verifiesModularStructure() {
        modules.verify();
    }
}
```

Now run this test, and it will fail.
You can see all the violations of modular structure in the console output.

```shell
- Module 'catalog' depends on non-exposed type com.sivalabs.bookstore.common.models.PagedResult within module 'common'!

- Module 'inventory' depends on non-exposed type com.sivalabs.bookstore.orders.domain.events.OrderCreatedEvent within module 'orders'!

- Module 'notifications' depends on non-exposed type com.sivalabs.bookstore.orders.domain.events.OrderCreatedEvent within module 'orders'!
```

Let's fix them.

**4. Make `common` module `OPEN` type module.**

Add `package-info.java` in `com.sivalabs.bookstore.common` package with the following content:

```java
@ApplicationModule(type = ApplicationModule.Type.OPEN)
package com.sivalabs.bookstore.common;

import org.springframework.modulith.ApplicationModule;
```

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

**6. Try to access another module's internal component.**

Autowire `ProductRepository` in `OrderServiceImpl`.

Run `ModularityTests` and the test should FAIL with the following error:

```shell
- Module 'orders' depends on non-exposed type com.sivalabs.bookstore.catalog.domain.ProductRepository within module 'catalog'!
```

**7. Explicitly specify/restrict module dependencies.**

Add `package-info.java` in `com.sivalabs.bookstore.orders` package with the following content:

```java
@ApplicationModule(allowedDependencies = {"catalog"})
package com.sivalabs.bookstore.orders;

import org.springframework.modulith.ApplicationModule;
```

**8. Try to create circular-dependency between two modules.**

Make `InventoryService` as a `public` class and autowire in `OrderServiceImpl`.

Run `ModularityTests` and the test should FAIL with the following error:

```shell
Cycle detected: Slice inventory -> 
                Slice orders -> 
                Slice inventory
```

**9. Testing modules independently using `@ApplicationModuleTest`**

Replace `@SpringBootTest` with `@ApplicationModuleTest`
in `ProductRestControllerTests`, `InventoryIntegrationTests`, `OrderRestControllerTests`.

**10. Verify event published or not.**

In `OrderRestControllerTests`, update `shouldCreateOrderSuccessfully()` test as follows:

```java
@Test
void shouldCreateOrderSuccessfully(AssertablePublishedEvents events) throws Exception {
    mockMvc.perform(
        post("/api/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(
            """
             ...
             ...
           """))
        .andExpect(status().isCreated());

    assertThat(events)
            .contains(OrderCreatedEvent.class)
            .matching(e -> e.customer().email(), "siva123@gmail.com")
            .matching(OrderCreatedEvent::productCode, "P100");
}
```

Check the `event_publication` table for the event processing history.

Explain external event publication support.

**11. Publish event and verify the expected behavior.**

In `InventoryIntegrationTests`, update `handleOrderCreatedEvent()` test as follows:

```java
@Test
void handleOrderCreatedEvent(Scenario scenario) {
    var customer = new Customer("Siva", "siva@gmail.com", "9987654");
    String productCode = "P114";
    var event = new OrderCreatedEvent(UUID.randomUUID().toString(), productCode, 2, customer);
    scenario.publish(event).andWaitForStateChange(() -> inventoryService.getStockLevel(productCode) == 598);
}
```

**12. Create C4 Model Documentation**

In `ModularityTests.java` add the `createModuleDocumentation()` test.

```java
package com.sivalabs.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModularityTests {
    static ApplicationModules modules = ApplicationModules.of(BookStoreApplication.class);

    @Test
    void verifiesModularStructure() {
        modules.verify();
    }

    @Test
    void createModuleDocumentation() {
        new Documenter(modules).writeDocumentation();
    }
}
```

The C4 Model documentation is generated under `target/spring-modulith-docs` directory.