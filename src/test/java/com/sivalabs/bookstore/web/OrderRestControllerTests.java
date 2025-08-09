package com.sivalabs.bookstore.web;

import com.sivalabs.bookstore.TestcontainersConfiguration;
import com.sivalabs.bookstore.models.CreateOrderRequest;
import com.sivalabs.bookstore.models.CreateOrderResponse;
import com.sivalabs.bookstore.models.Customer;
import com.sivalabs.bookstore.models.OrderItem;
import com.sivalabs.bookstore.services.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@AutoConfigureMockMvc
class OrderRestControllerTests {

    @Autowired
    private MockMvcTester mockMvcTester;

    @Autowired
    private OrderService orderService;

    @Test
    void shouldCreateOrderSuccessfully() {
        MvcTestResult testResult = mockMvcTester.post().uri("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "customer": {
                                "name": "Siva",
                                "email": "siva123@gmail.com",
                                "phone": "9876523456"
                           },
                            "deliveryAddress": "James, Bangalore, India",
                            "item":{
                                    "code": "P100",
                                    "name": "The Hunger Games",
                                    "price": 34.0,
                                    "quantity": 1
                            }
                        }
                        """)
                .exchange();
        assertThat(testResult).hasStatus(HttpStatus.CREATED);
    }

    @Test
    void shouldReturnNotFoundWhenOrderIdNotExist() {
        MvcTestResult testResult = mockMvcTester.get().uri("/api/orders/{orderNumber}", "non-existing-order-id").exchange();
        assertThat(testResult).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldGetOrderSuccessfully() {
        CreateOrderRequest request = buildCreateOrderRequest();
        CreateOrderResponse createOrderResponse = orderService.createOrder(request);

        MvcTestResult testResult = mockMvcTester.get().uri("/api/orders/{orderNumber}", createOrderResponse.orderNumber()).exchange();
        assertThat(testResult).hasStatus(HttpStatus.OK)
                .bodyJson()
                .extractingPath("$.orderNumber").isEqualTo(createOrderResponse.orderNumber());
    }

    @Test
    void shouldGetOrdersSuccessfully() {
        CreateOrderRequest request = buildCreateOrderRequest();
        orderService.createOrder(request);

        MvcTestResult testResult = mockMvcTester.get().uri("/api/orders").exchange();
        assertThat(testResult).hasStatus(HttpStatus.OK);
    }

    private static CreateOrderRequest buildCreateOrderRequest() {
        OrderItem item = new OrderItem("P100", "The Hunger Games", new BigDecimal("34.0"), 1);
        return new CreateOrderRequest(
                new Customer("Siva", "siva@gmail.com", "77777777"), "Siva, Hyderabad, India", item);
    }
}
