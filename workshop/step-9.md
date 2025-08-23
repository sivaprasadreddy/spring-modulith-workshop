# 9. Event Driven Communication

Prefer event-driven communication between modules.

## Using Spring's ApplicationEventPublisher

```java
@Service
class OrderService {
    private final ApplicationEventPublisher publisher;

    void createOrder() {
        OrderCreatedEvent event = new OrderCreatedEvent(...);
        publisher.publishEvent(event);
    }
}

@Component
class OrderCreatedEventHandler {
    @EventListener
    void handle(OrderCreatedEvent event) {
        log.info("Received order created event: {}", event);
    }
}
```

**Issue:** The event handler is executed in the same transaction as the event publication.

Demo: Place an Order and check the transaction in OrderService.createOrder() and OrderCreatedEventHandler.handle().

## Using TransactionalEventListener

```java
@Service
class OrderService {
    private final ApplicationEventPublisher publisher;

    void createOrder() {
        OrderCreatedEvent event = new OrderCreatedEvent(...);
        publisher.publishEvent(event);
    }
}

@Component
class OrderCreatedEventHandler {
    @Async 
    @TransactionalEventListener
    void handle(OrderCreatedEvent event) {
        log.info("Received order created event: {}", event);
    }
}
```

**Issue:** The event handler is executed in a separate transaction from the event publication.
So, if the event handler fails, the event may be lost.

## Using Spring Modulith Event Publishing

Add the following dependency to your project.

```xml
<dependency>
    <groupId>org.springframework.modulith</groupId>
    <artifactId>spring-modulith-events-api</artifactId>
</dependency>
```

Use the `@ApplicationModuleListener` annotation to register event handlers instead of `@EventListener`.

```java
import org.springframework.modulith.events.ApplicationModuleListener;

@Component
class OrderCreatedEventHandler {
    /*
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    */
    @ApplicationModuleListener
    void handle(OrderCreatedEvent event) {
        log.info("Received order created event: {}", event);
    }
}
```

Now run the test `OrderRestControllerTests.shouldCreateOrderSuccessfully()` test.
Using the [Spring Debugger plugin](https://plugins.jetbrains.com/plugin/25302-spring-debugger), you should be able to see that the event handler is executed in a separate transaction.

## The Event Publication Registry
The events can be persisted in a database so that they can be processed without losing then on application failures.

```xml
<dependency>
    <groupId>org.springframework.modulith</groupId>
    <artifactId>spring-modulith-starter-jdbc</artifactId>
</dependency>
```

```properties
spring.modulith.events.jdbc.schema-initialization.enabled=true
# completion-mode options: update | delete | archive
spring.modulith.events.completion-mode=update
spring.modulith.events.republish-outstanding-events-on-restart=true
```

Start the application, create an order and check the data in `event_publication` table.

## Externalizing Events
The events can also be published to an external messaging system like Kafka or RabbitMQ.

```xml
<dependency>
    <groupId>org.springframework.modulith</groupId>
    <artifactId>spring-modulith-events-amqp</artifactId>
</dependency>
```

```java
import org.springframework.modulith.events.Externalized;

@Externalized("BookStoreExchange::orders.new")
public record OrderCreatedEvent(String orderNumber) {}
```

* Login into RabbitMQ Admin Console http://localhost:15672 using the credentials `guest/guest`. 
* Go to Queues and Streams tab and check the `new-orders` queue.
* Create a new order
* You should see the event in the queue.

<p align="center">
[Previous: 8. Explicit module dependencies](step-8.md) &nbsp;&nbsp;&nbsp;&nbsp;
[Next: 10. Testing modules in isolation](step-10.md)
</p>