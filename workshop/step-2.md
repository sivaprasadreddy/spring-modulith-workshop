# 2. Follow package-by-feature

**1. Refactor code to follow package-by-feature**

The existing code can be refactored to have the following structure:

```
bookstore
  |- config
  |- common
  |- catalog
  |   - domain
  |   - web
  |- orders
  |   - domain
  |   - web
  |- inventory
```

Create the directory structure using the following command:

```shell
$ cd src/main/java/com/sivalabs/bookstore
$ mkdir -p {common/models,catalog/{domain,web},orders/{domain,web},inventory}
```


```
bookstore
  |- config
  |- common
      - models
            - PagedResult.java
  |- catalog
      - Product.java
  |   - domain
            - ProductEntity.java
            - ProductRepository.java
            - ProductService.java
            - ProductNotFoundException.java
  |   - web
            - ProductRestController.java
            - CatalogExceptionHandler.java
  |- orders
  |   - domain
            - OrderEntity.java
            - OrderRepository.java
            - OrderService.java
            - CreateOrderRequest.java
            - CreateOrderResponse.java
            - OrderMapper.java
            - OrderView.java
            - OrderDTO.java
            - OrderNotFoundException.java
            - InvalidOrderException.java
            - models
                 - Customer.java
                 - OrderItem.java
                 - OrderStatus.java
  |   - web
  |         - OrderRestController.java
  |         - OrdersExceptionHandler.java
  |- inventory
      - InventoryEntity.java
      - InventoryRepository.java
      - InventoryService.java
      - OrderEventsInventoryHandler.java
```

2. Split **GlobalExceptionHandler.java** into separate exception handlers for each module. 

**Create CatalogExceptionHandler.java**

```java
package com.sivalabs.bookstore.catalog.web;

@RestControllerAdvice
class CatalogExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    ProblemDetail handle(ProductNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Product Not Found");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
```

**Create OrdersExceptionHandler.java**

```java
package com.sivalabs.bookstore.orders.web;

@RestControllerAdvice
class OrdersExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    ProblemDetail handle(OrderNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Order Not Found");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(InvalidOrderException.class)
    ProblemDetail handle(InvalidOrderException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Invalid Order Creation Request");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
```

**3. Accordingly, move tests to the new package structure**


[Next: 3. Add Spring Modulith support](step-3.md)