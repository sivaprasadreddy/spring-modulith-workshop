package com.sivalabs.bookstore.web;

import com.sivalabs.bookstore.exceptions.OrderNotFoundException;
import com.sivalabs.bookstore.models.CreateOrderRequest;
import com.sivalabs.bookstore.models.CreateOrderResponse;
import com.sivalabs.bookstore.models.OrderDTO;
import com.sivalabs.bookstore.models.OrderView;
import com.sivalabs.bookstore.services.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
class OrderRestController {
    private static final Logger log = LoggerFactory.getLogger(OrderRestController.class);

    private final OrderService orderService;

    OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping(value = "/{orderNumber}")
    OrderDTO getOrder(@PathVariable(value = "orderNumber") String orderNumber) {
        log.info("Fetching order by orderNumber: {}", orderNumber);
        return orderService
                .findOrder(orderNumber)
                .orElseThrow(() -> OrderNotFoundException.forOrderNumber(orderNumber));
    }

    @GetMapping
    List<OrderView> getOrders() {
        return orderService.findOrders();
    }
}
