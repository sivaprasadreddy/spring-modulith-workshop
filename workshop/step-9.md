# 9. Event Driven Communication

* Prefer event-driven communication between modules.

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

```java
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

## The Event Publication Registry
The events can be persisted in a database so that they can be processed without losing then on application failures.

```xml
<dependency>
    <groupId>org.springframework.modulith</groupId>
    <artifactId>spring-modulith-events-api</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.modulith</groupId>
    <artifactId>spring-modulith-starter-jdbc</artifactId>
</dependency>
```

```properties
spring.modulith.events.jdbc.schema-initialization.enabled=true
spring.modulith.events.completion-mode=update|delete|archive
spring.modulith.events.republish-outstanding-events-on-restart=true
```

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

[Next: 10. Testing modules in isolation](step-10.md)